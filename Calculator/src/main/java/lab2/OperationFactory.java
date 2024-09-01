package lab2;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import lab2.operat.*;

class OperationFactory {

    private static final Logger logger = Logger.getLogger(App.class.getName());
    public Map<String, Class<? extends OperationInterface>> operationMap = new HashMap<>();

    public OperationFactory() {
        try {
            List<String> classNames = findAllClasses();
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Operation.class)) {
                    Operation annotation = clazz.getAnnotation(Operation.class);
                    operationMap.put(annotation.value(), (Class<? extends OperationInterface>) clazz);
                }
            }

            for (Map.Entry<String, Class<? extends OperationInterface>> entry : operationMap.entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            }
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    public OperationInterface getOperation(String className) throws IllegalAccessException, InstantiationException {
        Class<? extends OperationInterface> operationClass = operationMap.get(className);
        if (operationClass != null) {
            return operationClass.newInstance();
        } else {
            logger.warning("Unknown operation");
            throw new IllegalArgumentException("Unknown operation: " + className);
        }
    }

    public static List<String> findAllClasses() {
        final String classPath = System.getProperty("java.class.path", ".");
        final String[] classPathElements = classPath.split(File.pathSeparator);
        return Arrays.stream(classPathElements).map(OperationFactory::findAllClasses).flatMap(List::stream).collect(Collectors.toList());
    }

    private static List<String> findAllClasses(final String element) {
        final List<String> result = new ArrayList<>();
        final File file = new File(element);
        if (file.isDirectory()) {
            result.addAll(findClassesInFolder(file, file));
        } else {
            result.addAll(loadClassesFromJarFile(file));
        }
        return result;
    }

    private static List<String> loadClassesFromJarFile(final File file) {
        final List<String> result = new ArrayList<>();

        try (ZipFile zf = new ZipFile(file)) {
            final Enumeration e = zf.entries();
            while (e.hasMoreElements()) {
                final ZipEntry ze = (ZipEntry) e.nextElement();
                final String fileName = ze.getName();
                var pos = fileName.toLowerCase().indexOf(".class");
                if (pos > 0) {
                    result.add(fileName.replace('/', '.').substring(0, pos));
                }
            }
        } catch (final IOException e) {
            throw new Error(e);
        }
        return result;
    }

    private static List<String> findClassesInFolder(
            final File directory,
            final File baseDir) {
        final List<String> result = new ArrayList<>();
        final File[] fileList = directory.listFiles();
        for (final File file : fileList) {
            if (file.isDirectory()) {
                result.addAll(findClassesInFolder(file, baseDir));
            } else {
                final String fileName = file.getAbsolutePath().substring(baseDir.getAbsolutePath().length()+1);
                var pos = fileName.toLowerCase().indexOf(".class");
                if (pos > 0) {
                    result.add(fileName.replace(File.separator, ".").substring(0, pos));
                }
            }
        }
        return result;
    }
}
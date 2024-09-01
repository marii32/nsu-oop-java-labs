package fabric;

import fabric.supplier.BodySupplier;
import fabric.supplier.Supplier;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FactoryGUI {
    private JFrame frame;
    private JPanel panel;
    private JLabel accessoryLabel;
    private JLabel bodyLabel;
    private JLabel engineLabel;
    private JLabel totalAccessoriesLabel;
    private JLabel totalCarsLabel;
    private JLabel carsInStockLabel;
    private JSlider accessorySlider;
    private JSlider bodySlider;
    private JSlider engineSlider;
    private JSlider dealerSpeedSlider;

    private Timer timer;

    private Context context;


    public FactoryGUI(Context context, Controller controller) {

        this.context = context;

        frame = new JFrame("Factory Control Panel");
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.add(panel);

        accessoryLabel = new JLabel("Accessory Storage: " + context.storageAccessorySize);
        bodyLabel = new JLabel("Body Storage: " + context.storageBodySize);
        engineLabel = new JLabel("Engine Storage: " + context.storageEngineSize);
        totalAccessoriesLabel = new JLabel("Total Accessories: 0");
        totalCarsLabel = new JLabel("Total Cars: 0");
        carsInStockLabel = new JLabel("Cars in Storage: 0");

        accessorySlider = new JSlider(JSlider.HORIZONTAL, 0, 5000, context.accessoryInterval);
        accessorySlider.setMajorTickSpacing(1000);
        accessorySlider.setPaintTicks(true);
        accessorySlider.setPaintLabels(true);
        accessorySlider.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                int value = source.getValue();

                context.accessoryInterval = value;

            }
        });

        bodySlider = new JSlider(JSlider.HORIZONTAL, 0, 5000, context.bodyInterval);
        bodySlider.setMajorTickSpacing(1000);
        bodySlider.setPaintTicks(true);
        bodySlider.setPaintLabels(true);
        bodySlider.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {


                int value = source.getValue();
                context.bodyInterval = value;
                System.out.println(context.bodyInterval);
            }
        });

        engineSlider = new JSlider(JSlider.HORIZONTAL, 0, 5000, context.engineInterval);
        engineSlider.setMajorTickSpacing(1000);
        engineSlider.setPaintTicks(true);
        engineSlider.setPaintLabels(true);
        engineSlider.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                int value = source.getValue();
                context.engineInterval = value;

            }
        });

        dealerSpeedSlider = new JSlider(JSlider.HORIZONTAL, 0, 5000, context.dealerInterval);
        dealerSpeedSlider.setMajorTickSpacing(1000);
        dealerSpeedSlider.setPaintTicks(true);
        dealerSpeedSlider.setPaintLabels(true);
        dealerSpeedSlider.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                int value = source.getValue();
                context.dealerInterval = value;
            }
        });

        panel.add(accessoryLabel);
        panel.add(accessorySlider);
        panel.add(bodyLabel);
        panel.add(bodySlider);
        panel.add(engineLabel);
        panel.add(engineSlider);
        panel.add(totalAccessoriesLabel);
        panel.add(totalCarsLabel);
        panel.add(carsInStockLabel);
        panel.add(dealerSpeedSlider);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                controller.shutdown();
                System.exit(0);
            }
        });

        frame.pack();
        frame.setVisible(true);

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGUI(controller);
            }
        });
        timer.start();
    }

    private void updateGUI(Controller controller) {

        accessoryLabel.setText("Accessory Storage: " + controller.accessoryStorage.store.size());
        bodyLabel.setText("Body Storage: " + controller.bodyStorage.store.size());
        engineLabel.setText("Engine Storage: " + controller.engineStorage.store.size());
        totalAccessoriesLabel.setText("Total Accessories: " + context.accessoryCount);
        totalCarsLabel.setText("Total Cars: " + context.produsedCar);
        carsInStockLabel.setText("Cars in Stock: " + controller.autoStorage.store.size());
    }
}

package SecGame.Hero;


public class HeroController {

    public HeroModel model;

    public HeroController(int wight, int height, HeroModel model) {

        this.model = model;
    }


    public void handleKeyPress(String key) {


        model.moving = true;
        switch (key) {
            case "up":
                model.y -= model.speed;
                if (model.y < 0) {
                    model.y = 0;
                }
                break;
            case "down":
                model.y += model.speed;
                if (model.y > model.WinHeight - model.size) {
                    model.y = model.WinHeight - model.size;
                }
                break;
            case "left":
                model.x -= model.speed;
                if (model.x < 0) {
                    model.x = 0;
                }
                model.side = "left";
                break;
            case "right":
                model.x += model.speed;
                if (model.x > model.WinWidth - model.size) {
                    model.x = model.WinWidth - model.size;
                }
                model.side = "right";
                break;
        }
    }
}
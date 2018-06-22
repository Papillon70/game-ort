package bargiela.game;

import android.text.method.BaseKeyListener;
import android.util.Log;

import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.actions.interval.RotateTo;
import org.cocos2d.actions.interval.ScaleBy;
import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCColor3B;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCSize;

import java.util.Random;

public class GameClass {
    CCGLSurfaceView _GameView;
    CCSize ScreenDevice;

    Sprite Player;
    Sprite Background;
    Sprite Enemy;

    Label lblGame;
    Label lblPoints;

    int ActualPoints;

    public GameClass(CCGLSurfaceView GameView){
        Log.d("Bob","Begins constructor of class");
        _GameView = GameView;
    }

    public void BeginGame(){
        Log.d("Begin","Begins game");

        Director.sharedDirector().attachInView(_GameView);

        ScreenDevice = Director.sharedDirector().displaySize();
        Log.d("Begin","Screen size - width: "+ScreenDevice.width+" - Height: "+ScreenDevice.height);

        Log.d("Begin","Tell director to execute scene");
        Director.sharedDirector().runWithScene(sceneGame());

    }

    private Scene sceneGame(){
        Log.d("scene","Begins build up of scene");

        Log.d("scene","Declare and instance scene");
        Scene sceneToReturn;
        sceneToReturn = Scene.node();

        Log.d("scene","Declare and instance background layer");
        BackgroundLayer backgroundLayer;
        backgroundLayer = new BackgroundLayer();

        Log.d("scene","Declare and instance front layer");
        FrontLayer frontLayer;
        frontLayer = new FrontLayer();

        Log.d("scene","Add layers to scene");
        sceneToReturn.addChild(backgroundLayer, -1);
        sceneToReturn.addChild(frontLayer, 1);

        Log.d("scene","Return scene complete");
        return sceneToReturn;
    }

    class BackgroundLayer extends Layer {
        public BackgroundLayer(){
            Log.d("BackgroundLayer","Begins constructor of background layer");

            Log.d("BackgroundLayer","Insert background image");
            InsertBackgroundImage();
        }

        private void InsertBackgroundImage(){
            Log.d("InsertBackgroundImage","Start inserting background image");

            Log.d("InsertBackgroundImage","Instance sprite");
            Background = Sprite.sprite("background.png");

            Log.d("InsertBackgroundImage","Set image in the center of the screen");
            Background.setPosition(ScreenDevice.width/2, ScreenDevice.height/2);

            Log.d("InsertBackgroundImage","Enlarge image twice it's size");

            Float width, height;
            width = ScreenDevice.width / Background.getWidth();
            height = ScreenDevice.height / Background.getHeight();
            Background.runAction(ScaleBy.action(0.1f, width*2, height));

            Log.d("InsertBackgroundImage","Add background to layer");
            super.addChild(Background);
        }
    }

    class FrontLayer extends Layer {
        public FrontLayer(){
            Log.d("Front layer","Begins constructor of front layer");

            Log.d("Front layer","Set player in place");
            SetPlayerInPlace();
            super.schedule("addEnemy", 1.5f);
            //super.unschedule("addEnemy");
            SetGameTitle();
            super.schedule("IntersectionBetweenSprites",1.5f);
            Log.d("Front layer","Frontlayer set correctly");
        }

       public void SetPlayerInPlace(){
            Log.d("SetPlayerInPlace","Start putting player in place");

            Log.d("SetPlayerInPlace","Instance sprite");
            Player= Sprite.sprite("player.png");

            float InitialPositionX, InitialPositionY;
            InitialPositionX=ScreenDevice.width/2;
            InitialPositionY=Player.getHeight()/2;


            Log.d("SetPlayerInPlace","Set in X: "+InitialPositionX+" - Y: "+InitialPositionY);
            Player.setPosition(InitialPositionX,InitialPositionY);

            Player.runAction(ScaleBy.action(0.1f,1f,1f));

            Log.d("SetPlayerInPlace","Add player to layer");
            super.addChild(Player);
        }

        private void SetGameTitle(){
            Log.d("SetTitle","Start setting title");
            lblGame= Label.label("Baggio game", "Verdana", 50);
            lblPoints= Label.label("Points: "+ ActualPoints,"Verdana", 30);

            float GameHeight;
            GameHeight=lblGame.getHeight();

            float PointsHeight;
            PointsHeight=lblPoints.getHeight();

            lblGame.setPosition(ScreenDevice.width/2, ScreenDevice.height-GameHeight/2);
            lblPoints.setPosition(ScreenDevice.width/16, ScreenDevice.height-PointsHeight/2);

            Log.d("SetTitle","Add colour to title");
            CCColor3B colourPoints=new CCColor3B(128,100,200);

            Log.d("SetTitle","PRUEBA 1");

            lblPoints.setColor(colourPoints);

            Log.d("SetTitle","PRUEBA 2");

            super.addChild(lblGame);
            super.addChild(lblPoints);
            Log.d("SetTitle","Done setting title");

        }

        public void addEnemy(float TimeDifference){

            Log.d("AddEnemy","Instance enemy sprite");
            Enemy = Sprite.sprite("enemy.png");

            Log.d("AddEnemy","Set initial position");
            int InitialPositionX, InitialPositionY;
            Float EnemyHeight;
            EnemyHeight=Enemy.getHeight();
            Float EnemyWidth=Enemy.getWidth();
            InitialPositionY=(int)(ScreenDevice.height + EnemyHeight/2);

            Log.d("AddEnemy","Locate enemy randomly");
            Random randomGenerator;
            randomGenerator=new Random();
            InitialPositionX=randomGenerator.nextInt((int)  (ScreenDevice.width - EnemyWidth));
            InitialPositionX += EnemyWidth / 2;

            Log.d("AddEnemy","Locate enemy on axis stablished");
            Enemy.setPosition(InitialPositionX, InitialPositionY);

            Log.d("AddEnemy","Try rotating enemy");
            Enemy.runAction(RotateTo.action(3f, 540));


            Log.d("AddEnemy","Set final pos");
            int FinalPositionX, FinalPositionY;
            FinalPositionX=InitialPositionX;
            FinalPositionY=(int) - EnemyHeight/2;

            Log.d("AddEnemy","Give order to move until final position");
            Enemy.runAction(MoveTo.action(3, FinalPositionX, FinalPositionY));

            Log.d("AddEnemy","Add sprite to layer");
            super.addChild(Enemy);
        }

        boolean IntersectionBetweenSprites (Sprite sprite1, Sprite sprite2){
            boolean Return = false;

            int Sprite1left, Sprite1right, Sprite1down, Sprite1up;
            int Sprite2left, Sprite2right, Sprite2down, Sprite2up;

            Sprite1left = (int) (sprite1.getPositionX() - sprite1.getWidth()/2);
            Sprite1right = (int) (sprite1.getPositionX() + sprite1.getWidth()/2);
            Sprite1down = (int) (sprite1.getPositionY() - sprite1.getHeight()/2);
            Sprite1up = (int) (sprite1.getPositionY() + sprite1.getHeight()/2);

            Sprite2left = (int) (sprite2.getPositionX() - sprite2.getWidth()/2);
            Sprite2right = (int) (sprite2.getPositionX() + sprite2.getWidth()/2);
            Sprite2down = (int) (sprite2.getPositionY() - sprite2.getHeight()/2);
            Sprite2up = (int) (sprite2.getPositionY() + sprite2.getHeight()/2);

            Log.d("Intersection","SPRITE 1 - Izq: " + Sprite1left + " - Der: " + Sprite1right+" - Down: "+ Sprite1down+ " - Up: "+ Sprite1up);
            Log.d("Intersection","SPRITE 2 - Izq: " + Sprite2left + " - Der: " + Sprite2right+" - Down: "+ Sprite2down+ " - Up: "+ Sprite2up);


            return Return;
        }

        boolean IsBetween (int NumberToCompare, int Max, int Min){
            return true;
        }
    }
}




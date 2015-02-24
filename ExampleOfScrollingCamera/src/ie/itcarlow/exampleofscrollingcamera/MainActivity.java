package ie.itcarlow.exampleofscrollingcamera;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class MainActivity extends BaseGameActivity implements IUpdateHandler{
	
	//width and height of the camera "lens"
	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	
	//create a scene
	private Scene mScene;
	
	//animated sprite for the player
	private ITiledTextureRegion playerTiledTextureRegion;
	private AnimatedSprite playerSprite;
	
	//background sprite
	private ITextureRegion backgroundTextureRegion;
	private Sprite backgroundSprite;
	
	//the smooth camera
	SmoothCamera mSmoothCamera;

	@Override
	public EngineOptions onCreateEngineOptions()  
	{
		//params are x, y, width, height, xVelocity, yVelocity, zoom factor
		mSmoothCamera = new SmoothCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT,
				100, 0, 1.0f);
		
		//params are fullScreen, orientation, resolution and the camera we ant to use
		EngineOptions engine = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				mSmoothCamera);

		return engine;
		
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		loadGfx();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
		
	}
	
	private void loadGfx()
	{	
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		BitmapTextureAtlas backgroundTexture = new BitmapTextureAtlas(
				getTextureManager(), 3500, 480);
		backgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(backgroundTexture, this,
						"level1Background.png", 0, 0);
		backgroundTexture.load();
		
		BitmapTextureAtlas playerTexture = new BitmapTextureAtlas(getTextureManager(), 246, 35);
		playerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(playerTexture, this.getAssets(),
						"SpyroRun.png", 0, 0, 6, 1);
		playerTexture.load();
		
		

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		this.mScene = new Scene();
		
		pOnCreateSceneCallback.onCreateSceneFinished(this.mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		
		playerSprite = new AnimatedSprite(100, 300, playerTiledTextureRegion,
				this.getVertexBufferObjectManager());
		playerSprite.animate(150);
		playerSprite.setZIndex(10);
		this.mScene.attachChild(playerSprite);
		
		
		backgroundSprite = new Sprite(-300, 0, backgroundTextureRegion,
				this.mEngine.getVertexBufferObjectManager());
		backgroundSprite.setZIndex(7);
		mScene.attachChild(backgroundSprite);
		
		//sort the scene's children based on z values
		mScene.sortChildren();
		
		
		this.mEngine.registerUpdateHandler(this);
		
		//tell the camera to follow the player sprite
		mSmoothCamera.setChaseEntity(playerSprite);
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {

		playerSprite.setX(playerSprite.getX()+1f);
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}

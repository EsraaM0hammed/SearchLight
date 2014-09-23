package com.example.flashlight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	ImageButton btnswitch;
	private Camera camera;
	private boolean isFlashOn;
	private boolean hasFlash;
	Parameters parameters;
	MediaPlayer player;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnswitch = (ImageButton) findViewById(R.id.btnSwitch);

		hasFlash = getApplicationContext().getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

		// ·· «ﬂœ «–« ﬂ«‰ «·›·«‘ „ÊÃÊœ Ê·« ·«

		// -----------------------------------------------------------------------------------
		if (!hasFlash) {
			// device doesn't support flash
			// Show alert message and close the application

			AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
					.create();
			alertDialog.setTitle("Error!!");
			alertDialog
					.setMessage("Sorry, your device doesn't support flash light!");
			alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(final DialogInterface dialog, int which) {
					finish();

				}
			});
			alertDialog.show();
			return;

		}
		// ------------------------------------------------------------------------------------------
		getCamera();
		toggleButtonImage();

		// Switch button click event to toggle flash on/off
		btnswitch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isFlashOn) {
					// turn off flash
					turnOffFlash();
				} else {
					// turn on flash
					turnOnFlash();
				}

			}
		});

	}

	// get the camera

	private void getCamera() {
		if (camera == null) {
			try {
				camera = Camera.open();
				parameters = camera.getParameters();
			} catch (RuntimeException e) {
				Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
			}

		}

	}

	// turn on flash ...

	private void turnOnFlash() {
		if (!isFlashOn) {
			if (camera == null || parameters == null) {
				return;

			}
			playSound();
			parameters = camera.getParameters();
			parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(parameters);
			camera.startPreview();
			isFlashOn = true;
			// changing button/switch image
			toggleButtonImage();
		}

	}

	// turn off flash ...
	private void turnOffFlash() {
		if (isFlashOn) {
			if (camera == null || parameters == null) {
				
				 parameters = camera.getParameters();
		            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
		            camera.setParameters(parameters);
		            camera.stopPreview();
		            isFlashOn = false;
		            toggleButtonImage();

			}
			// play sound
			playSound();

			parameters = camera.getParameters();
			parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameters);
			camera.stopPreview();
			isFlashOn = false;

			// changing button/switch image
			toggleButtonImage();

		}

	}

	private void toggleButtonImage() {
		if (isFlashOn) {
			btnswitch.setImageResource(R.drawable.btn_switch_on);
		} else {
			btnswitch.setImageResource(R.drawable.btn_switch_off);

		}

	}

	private void playSound() {
		if (isFlashOn) {
			player = MediaPlayer.create(MainActivity.this,
					R.raw.light_switch_off);
		} else {
			player = MediaPlayer.create(MainActivity.this,
					R.raw.light_switch_off);
		}
		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {

				mp.release();
			}
		});
		player.start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onStart() {

		super.onStart();
		getCamera();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		turnOffFlash();
	}

	@Override
	protected void onResume() {

		super.onResume();
		/*if (hasFlash)
			turnOnFlash();*/
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
 
	@Override
	protected void onStop() {

		super.onStop();
		if (camera != null) {
			camera.release();
			camera = null;
		}
	}

}

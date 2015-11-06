package com.example.oquintansocampo.domotica20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.*;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.mycompany.domotica20.MENSAJE";
    boolean isFlashOn;
    Camera cam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;


            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                //  Toast.makeText(getApplicationContext(), "cambiando el progreso del seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //  Toast.makeText(getApplicationContext(), "Has movido la seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Vibrator vib = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(seekBar.getProgress() * 10);
                TextView textv = (TextView) findViewById(R.id.text2);
                textv.setText(String.valueOf("Progreso: " + progress));
                //Toast.makeText(getApplicationContext(), "Progreso: " + seekBar.getProgress() + "/" + seekBar.getMax(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(getApplicationContext(), "seekbar en reposo", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void sendMessage(View v) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText et = (EditText) findViewById(R.id.edit_message);
        String msg = et.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, msg);
        startActivity(intent);
    }

    public void llama(View v) {
        EditText editText = (EditText) findViewById(R.id.callField);
        String message = editText.getText().toString();
        Uri number = Uri.parse("tel:".concat(message));
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    public void navega(View v) {
        EditText editText = (EditText) findViewById(R.id.navField);
        String message = editText.getText().toString();
        Uri webpage = Uri.parse(message);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }

    public void localiza(View v) {
        EditText editText = (EditText) findViewById(R.id.mapField);
        String message = editText.getText().toString();
        // Map point based on address
        Uri location = Uri.parse("geo:0,0?q=" + message);
        // Or map point based on latitude/longitude
        // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

    public void forResult(View v) {
        Intent intent1 = new Intent(this, SecondActivity.class);
        startActivityForResult(intent1, 1);
    }

    public void flash(View v) {
        try {
            Button IO = (Button) findViewById(R.id.btLight);
            if (!isFlashOn) {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    cam = Camera.open();
                    Parameters p = cam.getParameters();
                    p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                    cam.setParameters(p);
                    cam.startPreview();
                    isFlashOn = true;
                    IO.setText("Apagar Linterna");
                }
            } else {
                cam.stopPreview();
                cam.release();
                isFlashOn = false;
                IO.setText("Encender Linterna");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception flashLightOn()",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("resultado");
                TextView tv = (TextView) findViewById(R.id.frField);
                tv.setText(result);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}


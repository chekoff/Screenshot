package com.chekoff.screenshot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final int[] count = {1};

            final View rootView = inflater.inflate(R.layout.fragment_screenshot, container, false);

            Button btnTakeScreenshot = (Button) rootView.findViewById(R.id.btnTakeScreenshot);
            final TextView txtCount = (TextView) rootView.findViewById(R.id.txtCount);
            final ImageView imgScreenshot = (ImageView) rootView.findViewById(R.id.imgScreenshot);

            btnTakeScreenshot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    count[0]++;
                    txtCount.setText("" + count[0]);

                    //take screenshot
                    LinearLayout layout_main = (LinearLayout) rootView.findViewById(R.id.layout_main);
                    String filePath = takeScreenshot(layout_main);

                    //show taken screenshot
                    Bitmap screenshot = BitmapFactory.decodeFile(filePath);
                    imgScreenshot.setImageBitmap(screenshot);

                }
            });

            return rootView;
        }

        public String takeScreenshot(LinearLayout linearLayout) {
            String imagePath;
            Bitmap myBitmap;

            //take screenshot
            View rootView = linearLayout.getRootView();
            rootView.setDrawingCacheEnabled(true);
            myBitmap = rootView.getDrawingCache();

            try {
                imagePath = saveImage(myBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }

            return imagePath;
        }

        public String saveImage(Bitmap bitmap) throws IOException {

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, bytes);
            File fileDir = new File(getActivity().getFilesDir(), "screenshots");
            fileDir.mkdirs();

            String fileName = "wf_" + System.currentTimeMillis() + ".png";
            //String fileName = "wf_screenshot" + ".png";

            File file = new File(fileDir, fileName);
            File[] files = fileDir.listFiles();
            for (File f : files) {
                f.delete();
            }
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();

            return file.getAbsolutePath();
        }
    }
}

package loading.geakw.com.loading;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class LoadingActivity extends ActionBarActivity implements View.OnClickListener{
    private ImageView loadingIv;
    LoadingDrawable loadingDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        findViewById(R.id.loadingBt).setOnClickListener(this);
        loadingIv = (ImageView) findViewById(R.id.loadintIv);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loading, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loadingBt:
                showLoading();
                break;
        }
    }

    private void showLoading() {
        if(loadingDrawable == null){
            loadingDrawable = new LoadingDrawable(loadingIv);
        }
        loadingIv.setImageDrawable(loadingDrawable);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(Color.parseColor("#ffffff"));
        loadingIv.setBackgroundDrawable(shapeDrawable);
        loadingDrawable.start();
    }

    private void cacelLoading(){
        if(loadingDrawable != null){
            loadingDrawable.stop();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cacelLoading();
    }
}

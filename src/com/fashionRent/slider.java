package com.fashionRent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.fashionRent.lib.dbHelper;
import com.fashionRent.lib.saveFile;

import java.io.File;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: brodjag
 * Date: 15.10.12
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */
public class slider extends Activity implements SensorEventListener {
   Activity con;
   String brandid="4";
    @Override
    public void onDestroy(){
        super.onDestroy();
       // System.gc();

        for(int i=0; i<flpViews.size(); i++){

        flpViews.get(i).removeAllViews();
        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); con=this;
        //System.gc();
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(!manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)){

            Toast.makeText(con,"err",Toast.LENGTH_SHORT).show();
           /// textView.setText("Error, could not register sensor listener");

        }

        setContentView(R.layout.slider);

        brandid=getIntent().getStringExtra("brandid");

        /*
        findViewById(R.id.slider_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {  ((Vibrator) con.getSystemService(con.VIBRATOR_SERVICE)).vibrate(50);} catch (Exception e) {   };
                startActivity(new Intent(con,disignerList.class));
                finish();
            }
        });
        */



        addItems();
        mkPoints();
        sliderChenge();
        chkImgs();
    }


    //запускает литсталку
    float  fromPosition=0;
private void sliderChenge(){


    LinearLayout container=(LinearLayout) findViewById(R.id.slider_container);
    final ViewFlipper flipper=(ViewFlipper) findViewById(R.id.slider_fliper);
    if(flipper.getChildCount()<2){return;}

    //кнопки
    findViewById(R.id.prev_bn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sliderForward(false);
        }
    });

    findViewById(R.id.next_bn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sliderForward(true);
        }
    });
    //конец конпки

    /*
    container.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN: // Пользователь нажал на экран, т.е. начало движения
                    // fromPosition - координата по оси X начала выполнения операции
                    fromPosition = event.getX();
                    break;
                case MotionEvent.ACTION_UP: // Пользователь отпустил экран, т.е. окончание движения
                    float toPosition = event.getX();
                    if (fromPosition > toPosition)  {
                        fromPosition = toPosition;
                        flipper.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_next_in));
                        flipper.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_next_out));
                        flipper.showNext();

                    }else {
                        // (fromPosition < toPosition)
                        fromPosition = toPosition;
                        flipper.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.go_prev_in));
                        flipper.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.go_prev_out));
                        flipper.showPrevious();
                    }
                    mkPoints();
                default:
                    break;
            }

            return true;
        }

    }); */

}

ArrayList<String> imgPfrontList=new ArrayList<String>();
ArrayList<String> imgPleftList=new ArrayList<String>();
ArrayList<String> imgPbackList=new ArrayList<String>();
ArrayList<String> imgPrightList=new ArrayList<String>();
ArrayList<ViewFlipper> flpViews=new ArrayList<ViewFlipper>();
ArrayList<String> ids=new ArrayList<String>();
ArrayList<View> sliderItem=new ArrayList<View>();



public Cursor getDressesCursor(){
    dbHelper db=new dbHelper(con);
  return   db.getDressesById(brandid);

};

private void addItems(){
    final ViewFlipper flipper=(ViewFlipper) findViewById(R.id.slider_fliper);

    final Cursor c= getDressesCursor();

    for(int i=0; i<c.getCount(); i++){
        c.moveToPosition(i);


        Log.d("sss", "" + c.getString(2));
     View v=con.getLayoutInflater().inflate(R.layout.slider_item,null);
        final String id=c.getString(0);
        dbHelper dh=new dbHelper(con);
        if(!dh.isFavorites(id)){v.findViewById(R.id.favorit_star).setVisibility(View.GONE);}

      ((TextView) v.findViewById(R.id.typename)).setText(c.getString(5));
        ((TextView) v.findViewById(R.id.brandname)).setText(Html.fromHtml(c.getString(3)));
        ((TextView) v.findViewById(R.id.desc)).setText(Html.fromHtml(c.getString(11)));
        ((TextView) v.findViewById(R.id.size)).setText(c.getString(10));
        final String p1=c.getString(12);
        ((TextView) v.findViewById(R.id.price1)).setText(p1+" руб.");
        final String p2=c.getString(13);
        ((TextView) v.findViewById(R.id.price2)).setText(p2+" руб.");

        //order 1
        v.findViewById(R.id.order1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {  ((Vibrator) con.getSystemService(con.VIBRATOR_SERVICE)).vibrate(50);} catch (Exception e) {   };
                Intent intent=new Intent(con, order.class);
                intent.putExtra("productId",id);
                intent.putExtra("rentTime",2);
                float priceValue=0;
                try{priceValue=Float.parseFloat("" + p1);}catch (Exception e){}
                intent.putExtra("priceValue",priceValue) ;

                float advance=2*Float.parseFloat("" + p1);
                intent.putExtra("advance",advance);
               startActivity(intent);
            }
        });

        //order 2
        v.findViewById(R.id.order2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {  ((Vibrator) con.getSystemService(con.VIBRATOR_SERVICE)).vibrate(50);} catch (Exception e) {   };

                Intent intent=new Intent(con, order.class);
                intent.putExtra("productId",id);
                intent.putExtra("rentTime",4);
                float priceValue=0;
                try{priceValue=Float.parseFloat("" + p2);}catch (Exception e){}
                intent.putExtra("priceValue",priceValue) ;

                float advance=2*Float.parseFloat("" + p1);
                intent.putExtra("advance",advance);
                startActivity(intent);


            }
        });

        final ViewFlipper f=(ViewFlipper) v.findViewById(R.id.img_flipper);
        flpViews.add(f);
        v.findViewById(R.id.flipper_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                f.showNext();
            }
        });
        sliderItem.add(v);
        flipper.addView(v);

        Log.d("urlq1",c.getString(7).toString());

        imgPfrontList.add(c.getString(6).toString());
          ids.add(c.getString(0).toString());
         imgPleftList.add(c.getString(7).toString());
         imgPbackList.add(c.getString(8).toString());
        imgPrightList.add(c.getString(9).toString());

    }

   c.close();
};

private void mkPoints(){
    LinearLayout container = (LinearLayout) findViewById(R.id.point_container);
    final ViewFlipper flipper=(ViewFlipper) findViewById(R.id.slider_fliper);
    container.removeAllViews();
    for(int i=0; i<flipper.getChildCount(); i++){
     if(i!=flipper.getDisplayedChild()) { getLayoutInflater().inflate(R.layout.point_white,container);
     }else {getLayoutInflater().inflate(R.layout.point,container);}
    }


}


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(con,disignerList.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
};



    public boolean isSDCardMounted() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }


    String urlTosd="/sdcard/fashionRent/";
    String urlToServer="http://fashionrent.ru/imgdb/160x400/";


    void chkImgs(){


        for(int i=0; i<imgPfrontList.size(); i++){
         Log.d("urlq",urlToServer+ imgPfrontList.get(i));
            File file = new File(urlTosd+imgPfrontList.get(i));
            if((file.exists())){
                setBitmap(urlTosd+imgPfrontList.get(i),  flpViews.get(i));
            }

            File fileL = new File(urlTosd+imgPfrontList.get(i));
            if((fileL.exists())){
                setBitmap(urlTosd+imgPleftList.get(i),  flpViews.get(i));
            }

            File fileb = new File(urlTosd+imgPbackList.get(i));
            if((fileb.exists())){
                setBitmap(urlTosd+imgPbackList.get(i),  flpViews.get(i));
            }

            File filer = new File(urlTosd+imgPrightList.get(i));
            if((filer.exists())){
                setBitmap(urlTosd+imgPrightList.get(i),  flpViews.get(i));
            }
            System.gc();
        }

        File merlDir = new File(urlTosd);
        boolean re= merlDir.mkdirs();
       new loadTask().execute("");

    }



    void setBitmap(String url, final ViewFlipper flp){
       // try{
        View v= con.getLayoutInflater().inflate(R.layout.grl_img, null);
       ImageView img= (ImageView) v.findViewById(R.id.img_i) ;

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(url, options);
        img.setImageBitmap(bm);
        ( flp).addView(v);
       // bm.recycle();
       // bm=null;
      //  System.gc();
      //  }catch (Exception e){}
        /*
        flp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {  ((Vibrator) con.getSystemService(con.VIBRATOR_SERVICE)).vibrate(50);} catch (Exception e) {   };
                flp.showNext();
                Log.d("fliper", "count "+flp.getChildCount());
            }
        });
       */
    }


    //load task
    private class loadTask extends AsyncTask<String,String,Boolean> {
       // public ProgressDialog waitDialog;
        @Override
        protected Boolean doInBackground(String... strings) {
            saveFile sf=new saveFile(con);
            //front
            for(int i=0; i<imgPfrontList.size(); i++){
                File file = new File(urlTosd+imgPfrontList.get(i));
                if((!file.exists())){
                    boolean r= sf.loadFile(urlToServer+ imgPfrontList.get(i),urlTosd+imgPfrontList.get(i));
                    Log.d("loadss",imgPfrontList.get(i));
                    if(r){ publishProgress(""+i,"f");}
                }
            }
            //left
            for(int i=0; i<imgPleftList.size(); i++){
                File file = new File(urlTosd+imgPleftList.get(i));
                if((!file.exists())){
                    boolean r= sf.loadFile(urlToServer+ imgPleftList.get(i),urlTosd+imgPleftList.get(i));
                    Log.d("loadss",imgPleftList.get(i));
                    if(r){ publishProgress(""+i,"l");}
                }
            }
            //back
            for(int i=0; i<imgPbackList.size(); i++){
                File file = new File(urlTosd+imgPbackList.get(i));
                if((!file.exists())){
                    boolean r= sf.loadFile(urlToServer+ imgPbackList.get(i),urlTosd+imgPbackList.get(i));
                    Log.d("loadss",imgPbackList.get(i));
                    if(r){ publishProgress(""+i,"b");}
                }
            }

            //right
            for(int i=0; i<imgPrightList.size(); i++){
                File file = new File(urlTosd+imgPrightList.get(i));
                if((!file.exists())){
                    boolean r= sf.loadFile(urlToServer+ imgPrightList.get(i),urlTosd+imgPrightList.get(i));
                    Log.d("loadss",imgPrightList.get(i));
                    if(r){ publishProgress(""+i,"r");}
                }
            }

        return true;
        }


        @Override
        protected void onPreExecute(){

        }
        @Override
        protected void onPostExecute(Boolean b){

        }

        @Override
        protected void onProgressUpdate(String... s) {
            int i=Integer.parseInt(s[0]);
           if(s[1].equals("f")) setBitmap(urlTosd+imgPfrontList.get(i), flpViews.get(i));
            if(s[1].equals("l")) setBitmap(urlTosd+imgPleftList.get(i), flpViews.get(i));
            if(s[1].equals("b")) setBitmap(urlTosd+imgPbackList.get(i), flpViews.get(i));
            if(s[1].equals("r")) setBitmap(urlTosd+imgPrightList.get(i), flpViews.get(i));
            //Toast.makeText(con,s[0],Toast.LENGTH_SHORT).show();
        }
    }
    //end load task

    /****************************
     menu
     *********************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
       // Toast.makeText(con,"aaa",Toast.LENGTH_LONG).show();

        inflater.inflate(R.menu.slider, menu);
        //menu.findItem(0)
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.favorit:
                favorit_add();
                return true;
            case R.id.favorit_dell:
                favorit_dell();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public boolean onPrepareOptionsMenu(Menu menu) {
        String id=ids.get(((ViewFlipper) findViewById(R.id.slider_fliper)).getDisplayedChild());
        Log.d("fav2",id);
        dbHelper dh=new dbHelper(con);
        if(dh.isFavorites(id)){
            menu.findItem(R.id.favorit).setVisible(false);
            menu.findItem(R.id.favorit_dell).setVisible(true);
        }else {
            menu.findItem(R.id.favorit).setVisible(true);
            menu.findItem(R.id.favorit_dell).setVisible(false);
        }
        return true;
    }


    private void favorit_add() {
     int i=((ViewFlipper) findViewById(R.id.slider_fliper)).getDisplayedChild();
     String id=ids.get(i);
     dbHelper dh=new dbHelper(con);
     dh.insertToFaviritesById(id);

     sliderItem.get(i).findViewById(R.id.favorit_star).setVisibility(View.VISIBLE);

    }

    private void favorit_dell() {
        int i=((ViewFlipper) findViewById(R.id.slider_fliper)).getDisplayedChild();
        String id=ids.get(i);
        dbHelper dh=new dbHelper(con);
        dh.dellFavById(id);
        sliderItem.get(i).findViewById(R.id.favorit_star).setVisibility(View.GONE);
    }

    void sliderForward(boolean a){
        final ViewFlipper flipper=(ViewFlipper) findViewById(R.id.slider_fliper);
        if (a)  {
           // fromPosition = toPosition;
            flipper.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_next_in));
            flipper.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_next_out));
            flipper.showNext();

        }else {
            // (fromPosition < toPosition)
            //fromPosition = toPosition;
            flipper.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.go_prev_in));
            flipper.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.go_prev_out));
            flipper.showPrevious();
        }
        mkPoints();

    }

    long t=0;
    @Override
    public void onSensorChanged(SensorEvent event) {


        long seconds = System.currentTimeMillis();
       // Log.d("acc1","t="+t+" sec="+seconds) ;
        if((seconds-t)<1000){return;}
        t=seconds;

        ViewFlipper flipper=(ViewFlipper) findViewById(R.id.slider_fliper);
        if(flipper.getChildCount()<2){return;}
        if (event.values[0]>6){

            flipper.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_next_in));
            flipper.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.go_next_out));
            flipper.showNext();
            mkPoints();

        }

        if (event.values[0]<-6){

            flipper.setInAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.go_prev_in));
            flipper.setOutAnimation(AnimationUtils.loadAnimation(getBaseContext(),R.anim.go_prev_out));
            flipper.showPrevious();
            mkPoints();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
package com.example.administrator.project207.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.administrator.project207.R;
import com.example.administrator.project207.utils.CheckPermission;
import com.example.administrator.project207.utils.Constants;
import com.example.administrator.project207.utils.ServerRequestQueue;

import org.json.JSONObject;

public class MyinfoActivity extends AppCompatActivity {

    private ServerRequestQueue mRequestQueue;
    private AlertDialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        mRequestQueue = ServerRequestQueue.getInstance(getApplicationContext());

        Button Editbutton = (Button) findViewById(R.id.editbutton);
        Button Finishbutton = (Button) findViewById(R.id.finishbutton);

        final EditText IDtext = (EditText) findViewById(R.id.idtext);
        final EditText Nametext = (EditText) findViewById(R.id.nametext);
        final EditText Birthtext = (EditText) findViewById(R.id.Birthtext);
        final EditText Phonetext = (EditText) findViewById(R.id.Phonetext);
        final ImageView UserImage = (ImageView) findViewById(R.id.UserImage);
        final Button addProfile = (Button) findViewById(R.id.addProfile);
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String userBirth = intent.getStringExtra("userBirth");
        String userNumber = intent.getStringExtra("userNumber");

/*
        if(MyPhoto != && !MyPhoto.equals("")){
            BitmapFactory.Options options = new BitmapFactory.Options() ;
            options.inSampleSize = 10 ;
            Bitmap bitmap = BitmapFactory.decodeFile(MyPhoto, options);
            if(bitmap != null)
            {
                UserImage.setImageBitmap(bitmap);
            }


        }
*/

        addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission checkPermission = (CheckPermission)getApplicationContext();
                if(checkPermission.readStoragePermission){
                    Intent intent1 = new Intent(Intent.ACTION_PICK);
                    intent1.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent1.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent1,10);

                }
                else{

                    Toast t = Toast.makeText(MyinfoActivity.this, "갤러리 접근 허용버튼을 눌러주세요", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
/*
        @Override
         protected void onActivityResult(int requestCode, int resultCode, Intent data){
            if(requestCode ==10 && resultCode==RESULT_OK){
                try {
                    String[] Projection = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(data.getData(), Projection, null, null, MediaStore.Images.Media.DATE_MODIFIED);
                    cursor.moveToFirst();

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 10 ;
                    Bitmap bitmap =BitmapFactory.decodeFile(cursor.getString(0), options);
                    if(bitmap != null){
                        UserImage.setImageBitmap(bitmap);

                    }

                    DBHelper helper =new DBHelper(this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    db.execSQL("update tb_student set photo =? where _id=?", new String[] {cursor.getString(0), String.valueOf(UserImage)});
                    db.close();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
*/
        IDtext.setText(userID);
        IDtext.setEnabled(false);
        IDtext.setBackgroundColor(getResources().getColor(R.color.colorGray));

        Nametext.setText(userName);
        Nametext.setEnabled(false);
        Nametext.setBackgroundColor(getResources().getColor(R.color.colorGray));

        Birthtext.setText(userBirth);
        Birthtext.setEnabled(false);
        Birthtext.setBackgroundColor(getResources().getColor(R.color.colorGray));

        Phonetext.setText(userNumber);
        Phonetext.setEnabled(false);
        Phonetext.setBackgroundColor(getResources().getColor(R.color.colorGray));


        Editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder =  new AlertDialog.Builder(MyinfoActivity.this);
                builder.setMessage("정보를 수정하겠습니까?")
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Nametext.setEnabled(false);
                                Birthtext.setEnabled(false);
                                Phonetext.setEnabled(false);
                            }
                        })
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Nametext.setEnabled(true);
                                Birthtext.setEnabled(true);
                                Phonetext.setEnabled(true);

                            }
                        })
                        .create()
                        .show();
            }
        });

        Finishbutton.setOnClickListener(new View.OnClickListener() {

            String userID = IDtext.getText().toString();
            String userName = Nametext.getText().toString();
            String userBirth = Birthtext.getText().toString();
            String userNumber = Phonetext.getText().toString();

            @Override
            public void onClick(View v) {



                      if(IDtext.equals("") | Nametext.equals("") | Birthtext.equals("") | Phonetext.equals("")){

                          AlertDialog.Builder builder =  new AlertDialog.Builder(MyinfoActivity.this);
                          builder.setMessage("빈칸없이 정보를 입력해주세요")
                                  .setPositiveButton("확인",null)
                                  .create()
                                  .show();

                      }
                      else{

                          AlertDialog.Builder builder =  new AlertDialog.Builder(MyinfoActivity.this);
                          builder.setTitle("개인정보변경")
                                  .setMessage("정보를 변경하시겠습니까?")
                                  .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialog, int which) {



                                          Nametext.setEnabled(false);
                                          Birthtext.setEnabled(false);
                                          Phonetext.setEnabled(false);

                                      }
                                  })
                                  .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialog, int which) {


                                          Response.Listener responseListener = new Response.Listener<String>(){

                                              @Override
                                              public void onResponse(String response) {


                                                  try{
                                                      JSONObject jsonObject = new JSONObject(response);
                                                      boolean success = jsonObject.getBoolean("success");

                                                      if(success){

                                                          AlertDialog.Builder builder =  new AlertDialog.Builder(MyinfoActivity.this);
                                                          builder.setMessage("정보변경에 성공했습니다")
                                                                  .setPositiveButton("확인",null);
                                                          finish();

                                                      }
                                                      else
                                                      {
                                                          AlertDialog.Builder builder =  new AlertDialog.Builder(MyinfoActivity.this);
                                                          builder.setMessage("정보변경에 실패했습니다. 다시 확인해주세요")
                                                                  .setPositiveButton("확인",null);
                                                      }




                                                  }
                                                  catch (Exception e){
                                                      e.printStackTrace();
                                                  }

                                              }
                                          };

                                          mRequestQueue.addRequest(Constants.POST_REQUEST_URLS.ACCOUNT_EDIT, responseListener, null, userID, userName, userBirth, userNumber);

                                      }
                                  })
                                  .create()
                                  .show();

                      }




            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;


        }
    }

    private long lastTimeBackPressed;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500)
        {
            finish();
            return;
        }
        Toast.makeText(this, "’뒤로’ 버튼을 한번 더 누르면 종료합니다." ,Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}

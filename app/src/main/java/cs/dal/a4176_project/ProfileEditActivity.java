package cs.dal.a4176_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Roxanne on 2017/11/13 0013.
 */

public class ProfileEditActivity extends AppCompatActivity {

    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    private static int output_X = 480;
    private static int output_Y = 480;

    private ImageView headImage = null;

    private Button selectPic, submit;

    private String userChoosenTask;
    DatabaseManager db;
    private EditText name, age, height, weight, medical;
    private CheckBox gender_male, gender_female;
    String s_name, s_age, s_gender, s_height, s_weight, s_medical;
    Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        //start database connection
        db = new DatabaseManager(getBaseContext());
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        selectPic = findViewById(R.id.SelectPic);
        //button for user to select image
        headImage = findViewById(R.id.imageView);

        selectPic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectImage();
            }
        });

        name = findViewById(R.id.name2);
        age = findViewById(R.id.age2);
        gender_male = findViewById(R.id.gender2);
        gender_female = findViewById(R.id.gender3);
        height = findViewById(R.id.height2);
        weight = findViewById(R.id.weight2);
        medical = findViewById(R.id.medicalhistory2);
        submit = findViewById(R.id.submit);
        gender_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the information user input
                if (!gender_female.isChecked())
                    s_gender = "male";
                else
                    toast.makeText(getBaseContext(), "Please choose only one", Toast.LENGTH_LONG).show();
                    //only enable one choice
            }
        });
        gender_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get user's input
                if (!gender_male.isChecked())
                    s_gender = "female";
                else
                    toast.makeText(getBaseContext(), "Please choose only one", Toast.LENGTH_LONG).show();
                //only enable one choice
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get user's input and manage the format
                s_name = name.getText().toString();
                s_age = age.getText().toString();
                s_height = height.getText().toString();
                s_weight = weight.getText().toString();
                s_medical = medical.getText().toString();
                Cursor cursor = db.getAllProfile();
                //get the profile information from the database
                if (cursor.moveToFirst()) {
                    //update data
                    editData();
                    db.close();
                } else {
                    //insert data
                    addData();
                    db.close();
                }
                toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG).show();
                //tell users they have successfully enter their profile
                headImage.buildDrawingCache();
                Bitmap bitmap = headImage.getDrawingCache();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //change the compress format of the picture for head photo
                byte[] byteArray = stream.toByteArray();

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("pic", byteArray);
                startActivity(intent);
            }

        });
    }
    //select photo from library or take photo
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        //dialog that give user choices
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEditActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(ProfileEditActivity.this);
                //user choose take photo
                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        //open front camera and take picture
                        choseHeadImageFromCameraCapture();

                }//user choose from library
                else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        //open image gallery
                        choseHeadImageFromGallery();

                }//user choose cancel
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // set file type
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // active camera
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // check if SDcard is available
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
        //request camera function
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        // if no reaction, cancel and return
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "canceled", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            //user choose from library
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;
            //user choose to take picture
            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "No SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    //crop the original pic
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // set
        intent.putExtra("crop", "true");

        // aspectX , aspectY :width and heigt
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    //get the pic and set as image view
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            headImage.setImageBitmap(photo);
        }
    }

    //method to check whether there is an SD card
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    //add data to database
    private void addData() {
        db.addProfile(s_name, s_age, s_gender, s_height, s_weight, s_medical);
    }
    //edit data from the database
    private void editData() {
        db.updateProfile(1, s_name, s_age, s_gender, s_height, s_weight, s_medical);
    }




}


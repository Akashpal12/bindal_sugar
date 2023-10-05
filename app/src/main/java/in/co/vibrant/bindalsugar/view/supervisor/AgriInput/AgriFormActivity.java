package in.co.vibrant.bindalsugar.view.supervisor.AgriInput;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.co.vibrant.bindalsugar.DB.DBHelper;
import in.co.vibrant.bindalsugar.R;
import in.co.vibrant.bindalsugar.adapter.GrowerSearchListAdapter;
import in.co.vibrant.bindalsugar.adapter.VillageSearchListAdapter;
import in.co.vibrant.bindalsugar.model.GrowerModel;
import in.co.vibrant.bindalsugar.model.VillageModal;
import in.co.vibrant.bindalsugar.util.AlertDialogManager;
import in.co.vibrant.bindalsugar.util.UtilFile;


public class AgriFormActivity extends AppCompatActivity implements MFS100Event {
    private static final String TAG = "AddFormActivity";
    TextView  tv_VillageCode, tv_growerCode, tv_Name, tv_growerFatherName;
    private List<VillageModal> villageMasterModels = new ArrayList<>();
    private List<GrowerModel> growerMasterModels = new ArrayList<>();
    AutoCompleteTextView autoTV_villageCode, autoTV_growerCode;
    private DBHelper db;
    // VillageAdapterAutoSearch adapter;
    VillageSearchListAdapter villageSearchListAdapter = null;
    GrowerSearchListAdapter gAdaper=null;
    String mVillageCodeSalected = "";
    String mVillageNameSalected = "";
    String mGrowerCodeSalected = "";
    String mSelectedName = "";
    String mSelectedFather = "";
    private Handler handler, handlerGrower;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private static final int TRIGGER_AUTO_COMPLETE_G = 100;
    private static final long AUTO_COMPLETE_DELAY_G = 300;
    ImageView ivPhoto_1, ivPhoto_2;
    Button btnChoosePhoto_1, btnChoosePhoto_2, btnCancel_1, btnCancel_2, btn_save_signature, btn_clear_signature, btn_save_add_form;
    LinearLayout lLayCamera_1, lLayRemove_1, lLayCamera_2, lLayRemove_2;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int PERMISSION_REQUEST_CODE_1 = 100;
    private static final int PERMISSION_REQUEST_CODE_2 = 200;

    BottomSheetDialog dialog_1;
    BottomSheetDialog dialog_2;
    ImageView finger_icon;
    Button reset_thumb;

    String isClicked = "";
    boolean isSaved = false;
    byte[] byteArray_photo_1, byteArray_photo_2;
    SignaturePad signaturePad;
    byte[] byteArraySignature = null;
    byte[] byteArrayThumb = null;
    //UserAllInfo userAllInfo;


    private static long mLastClkTime = 0;
    private static long Threshold = 1500;

    private enum ScannerAction {
        Capture, Verify
    }

    byte[] Enroll_Template;
    byte[] Verify_Template;
    private FingerData lastCapFingerData = null;
    ScannerAction scannerAction = ScannerAction.Capture;

    int timeout = 10000;
    MFS100 mfs100 = null;

    private boolean isCaptureRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_form);
        //Util.hideKeyboard(AddFormActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Agri Input Form");
        toolbar.setTitle("Agri Input Form");
        db = new DBHelper(this);

        try {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }

        try {
            mfs100 = new MFS100(this);
            mfs100.SetApplicationContext(AgriFormActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
    }

    @Override
    protected void onStart() {
        try {
            if (mfs100 == null) {
                mfs100 = new MFS100(this);
                mfs100.SetApplicationContext(AgriFormActivity.this);
            } else {
                InitScanner();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStart();
    }

    protected void onStop() {
        try {
            if (isCaptureRunning) {
                int ret = mfs100.StopAutoCapture();
            }
            Thread.sleep(500);
            //            UnInitScanner();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        try {
            if (mfs100 != null) {
                mfs100.Dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void init() {
        reset_thumb = findViewById(R.id.reset_thumb);
        finger_icon = findViewById(R.id.finger_icon);
        //tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        //tvHeaderTitle.setText(getString(R.string.agri_input_form));
        //tv_Back = findViewById(R.id.tv_Back);
        //tv_Back.setVisibility(View.VISIBLE);
        /*tv_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        reset_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSyncCapture();
            }
        });

        autoTV_villageCode = findViewById(R.id.autoTV_villageCode);
        autoTV_growerCode = findViewById(R.id.autoTV_growerCode);

        tv_VillageCode = findViewById(R.id.tv_VillageCode);
        tv_growerCode = findViewById(R.id.tv_growerCode);
        tv_Name = findViewById(R.id.tv_Name);
        tv_growerFatherName = findViewById(R.id.tv_growerFatherName);

        try {
            if (db != null) {
                villageMasterModels.clear();
                growerMasterModels.clear();
                villageMasterModels.addAll(db.getVillageModal(""));
                growerMasterModels.addAll(db.getGrowerModel("",""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ivPhoto_1 = findViewById(R.id.ivPhoto_1);
        ivPhoto_2 = findViewById(R.id.ivPhoto_2);
        btnChoosePhoto_1 = findViewById(R.id.btnChoosePhoto_1);
        btnChoosePhoto_2 = findViewById(R.id.btnChoosePhoto_2);

        signaturePad = (SignaturePad) findViewById(R.id.signaturePad);
        btn_save_signature = findViewById(R.id.btn_save_signature);
        btn_clear_signature = findViewById(R.id.btn_clear_signature);
        btn_save_add_form = findViewById(R.id.btn_save_add_form);


        villageSearchListAdapter = new VillageSearchListAdapter(this, R.layout.all_list_row_item_search, villageMasterModels);
        autoTV_villageCode.setThreshold(1);
        autoTV_villageCode.setAdapter(villageSearchListAdapter);

        autoTV_villageCode.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        VillageModal mVillgModel = (VillageModal) parent.getItemAtPosition(position);
                        mVillageCodeSalected = mVillgModel.getCode();
                        mVillageNameSalected = mVillgModel.getName();
                        autoTV_villageCode.setText(mVillgModel.getCode() + "-" + mVillgModel.getName());
                        tv_VillageCode.setText(mVillgModel.getCode() + "-" + mVillgModel.getName());
                        growerMasterModels.clear();
                        growerMasterModels.addAll(db.getGrowerModel(mVillageCodeSalected,""));
                        gAdaper = new GrowerSearchListAdapter(AgriFormActivity.this, R.layout.all_list_row_item_search_grower, growerMasterModels);
                        autoTV_growerCode.setThreshold(1);
                        autoTV_growerCode.setAdapter(gAdaper);
                        Log.d(TAG, "onItemClick: " + mVillgModel.getName());
                    }
                });
        autoTV_villageCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                villageSearchListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //  tv_VillageCode.setText("");
            }
        });


        autoTV_growerCode.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        GrowerModel mGrowerMaster = (GrowerModel) parent.getItemAtPosition(position);
                        mGrowerCodeSalected = mGrowerMaster.getGrowerCode();
                        mSelectedName = mGrowerMaster.getGrowerName();
                        mSelectedFather = mGrowerMaster.getGrowerFather();
                        autoTV_growerCode.setText(mGrowerMaster.getGrowerCode() + "-" + mGrowerMaster.getGrowerName() + "-" + mGrowerMaster.getGrowerFather());

                        tv_Name.setText(mSelectedName);
                        tv_growerCode.setText("Grower code : " + mGrowerCodeSalected);
                        tv_growerFatherName.setText("Father name : " + mSelectedFather);

                        Log.d(TAG, "onItemClick: " + "mGrowerCodeSalected--" + mGrowerCodeSalected);
                    }
                });


        autoTV_growerCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                gAdaper.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*tv_growerCode.setText("");
                tv_Name.setText("");
                tv_growerFatherName.setText("");
                tv_mobile.setText("");*/
            }
        });


        btnChoosePhoto_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBottomSheet1();
            }
        });
        btnChoosePhoto_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBottomSheet2();
            }
        });

        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                // code to handle the onStartSigning event
            }

            @Override
            public void onSigned() {
                // code to handle the onSigned event
                enableDisableButtons(true);
            }

            @Override
            public void onClear() {
                // code to handle the onClear event
                enableDisableButtons(false);
            }
        });
        //For Signature
        btn_save_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilFile.checkRequestPermiss(getApplicationContext(), AgriFormActivity.this)) {
                    isClicked = "Signature";
                    doPermissionGranted(); // isClicked = "camera2";
                }
            }
        });

        btn_clear_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
                //new AlertDialogManager().showToast(AddFormActivity.this, "Signature cleared");
            }
        });

        btn_save_add_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (mVillageCodeSalected.length() > 0 && mVillageCodeSalected != null && !mVillageCodeSalected.isEmpty()) {
                        if (mGrowerCodeSalected.length() > 0 && mGrowerCodeSalected != null && !mGrowerCodeSalected.isEmpty()) {
                            if (byteArray_photo_1 != null) {
                                if (byteArray_photo_2 != null) {
                                    if (byteArraySignature != null) {
                                        if (byteArrayThumb != null)
                                        {
                                            if (db != null) {
                                                int mAddAgriInputID = (int) db.insertAgriInputFormData(mVillageCodeSalected, mVillageNameSalected, mGrowerCodeSalected, mSelectedName, "", mSelectedFather, "", byteArray_photo_1, byteArray_photo_2, byteArraySignature, byteArrayThumb,  UtilFile.myTableId);
                                                Log.d(TAG, "onClick: " + ">>>>>>>" + "New Record" + mAddAgriInputID);
                                                UtilFile.villageCode = mVillageCodeSalected;
                                                UtilFile.growerCode = mGrowerCodeSalected;
                                                new AlertDialogManager().showToast(AgriFormActivity.this, getResources().getString(R.string.save_sucee));
                                                signaturePad.clear();
                                                ivPhoto_1.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_person_outline_24));
                                                ivPhoto_2.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                                mVillageCodeSalected = "";
                                                mGrowerCodeSalected = "";
                                                mVillageNameSalected = "";
                                                mSelectedName = "";
                                                mSelectedFather = "";
                                                autoTV_villageCode.setText("");
                                                autoTV_growerCode.setText("");
                                                byteArray_photo_1 = null;
                                                byteArray_photo_2 = null;
                                                byteArraySignature = null;
                                                byteArrayThumb = null;
                                                getGoToNextActivty();

                                            }
                                        }
                                        else
                                        {
                                            new AlertDialogManager().showToast(AgriFormActivity.this, "Thumb not found");
                                        }
                                    } else {
                                        new AlertDialogManager().showToast(AgriFormActivity.this, "Signature not found");
                                    }
                                } else {
                                    new AlertDialogManager().showToast(AgriFormActivity.this, "Please select photo 2");
                                }
                            } else {
                                new AlertDialogManager().showToast(AgriFormActivity.this, "Please select photo 1");
                            }
                        } else {
                            new AlertDialogManager().showToast(AgriFormActivity.this, getResources().getString(R.string.enter_grower));
                        }
                    } else {
                        new AlertDialogManager().showToast(AgriFormActivity.this, getResources().getString(R.string.enter_village));
                    }

            }
        });
    }


    private void getGoToNextActivty() {
        Intent intent = new Intent(AgriFormActivity.this, AddFormDetailActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
    }


    //For Camera 1
    private void getBottomSheet1() {
        View view = getLayoutInflater().inflate(R.layout.camera_bottom_sheet_1, null);
        dialog_1 = new BottomSheetDialog(this);
        dialog_1.setContentView(view);
        lLayCamera_1 = dialog_1.findViewById(R.id.lLayCamera_1);
        lLayRemove_1 = dialog_1.findViewById(R.id.lLayRemove_1);
        btnCancel_1 = dialog_1.findViewById(R.id.btnCancel_1);
        //click for camera Image 1
        lLayCamera_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilFile.checkRequestPermiss(getApplicationContext(), AgriFormActivity.this)) {
                    Log.d(TAG, "onClick: " + "permission already granted");
                    isClicked = "camera1";
                    doPermissionGranted();
                }
            }
        });

        lLayRemove_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (byteArray_photo_1 == null) {
                    new AlertDialogManager().showToast(AgriFormActivity.this, "Please choose photo 1");
                } else {
                    ivPhoto_1.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                    byteArray_photo_1 = null;
                    new AlertDialogManager().showToast(AgriFormActivity.this, "Successfully Removed photo 1");
                    dialog_1.dismiss();
                }
            }
        });
        btnCancel_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_1.dismiss();
            }
        });

        dialog_1.show();
    }

    private void getBottomSheet2() {
        View view = getLayoutInflater().inflate(R.layout.camera_bottom_sheet_2, null);
        dialog_2 = new BottomSheetDialog(this);
        dialog_2.setContentView(view);

        lLayCamera_2 = dialog_2.findViewById(R.id.lLayCamera_2);
        lLayRemove_2 = dialog_2.findViewById(R.id.lLayRemove_2);
        btnCancel_2 = dialog_2.findViewById(R.id.btnCancel_2);

        lLayCamera_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilFile.checkRequestPermiss(getApplicationContext(), AgriFormActivity.this)) {
                    isClicked = "camera2";
                    doPermissionGranted();
                }
            }
        });

        lLayRemove_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (byteArray_photo_2 == null) {
                    new AlertDialogManager().showToast(AgriFormActivity.this, "Please choose photo 2");
                } else {
                    ivPhoto_2.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                    byteArray_photo_2 = null;
                    new AlertDialogManager().showToast(AgriFormActivity.this, "Successfully Removed photo 2");
                    dialog_2.dismiss();
                }
            }
        });
        btnCancel_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_2.dismiss();
            }
        });
        dialog_2.show();
    }

    private void enableDisableButtons(boolean enableButton) {
        btn_save_signature.setEnabled(enableButton);
        btn_clear_signature.setEnabled(enableButton);
    }

    private void doPermissionGranted() {
        if (isClicked.equals("camera1")) {
            Log.d(TAG, "doPermissionGranted: " + "1 clicked");
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(pictureIntent, PERMISSION_REQUEST_CODE_1);
            }
        } else if (isClicked.equals("camera2")) {
            Log.d(TAG, "doPermissionGranted: " + "2 clicked");
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(pictureIntent, PERMISSION_REQUEST_CODE_2);
            }
        } else if (isClicked.equals("Signature")) {
            try {
                Bitmap bitmapSignature = signaturePad.getSignatureBitmap();
                // Create image from bitmap and store it in memory
                // convert to byte[]
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapSignature.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byteArraySignature = byteArrayOutputStream.toByteArray();
                //new AlertDialogManager().showToast(AddFormActivity.this, getResources().getString(R.string.signature_save_succ));
                Log.d(TAG, "doPermissionGranted: " + byteArraySignature);

                SaveAllDataInDB();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void SaveAllDataInDB()
    {
        if (mVillageCodeSalected.length() > 0 && mVillageCodeSalected != null && !mVillageCodeSalected.isEmpty()) {
            if (mGrowerCodeSalected.length() > 0 && mGrowerCodeSalected != null && !mGrowerCodeSalected.isEmpty()) {
                if (byteArray_photo_1 != null) {
                    if (byteArray_photo_2 != null) {
                        if (byteArraySignature != null) {
                            if (byteArrayThumb != null)
                            {
                                if (db != null) {
                                    int mAddAgriInputID = (int) db.insertAgriInputFormData(mVillageCodeSalected, mVillageNameSalected, mGrowerCodeSalected, mSelectedName, "", mSelectedFather, "", byteArray_photo_1, byteArray_photo_2, byteArraySignature, byteArrayThumb,  UtilFile.myTableId);
                                    Log.d(TAG, "onClick: " + ">>>>>>>" + "New Record" + mAddAgriInputID);
                                    UtilFile.villageCode = mVillageCodeSalected;
                                    UtilFile.growerCode = mGrowerCodeSalected;
                                    new AlertDialogManager().showToast(AgriFormActivity.this, getResources().getString(R.string.save_sucee));
                                    signaturePad.clear();
                                    ivPhoto_1.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_person_outline_24));
                                    ivPhoto_2.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_image_24));
                                    mVillageCodeSalected = "";
                                    mGrowerCodeSalected = "";
                                    mVillageNameSalected = "";
                                    mSelectedName = "";
                                    mSelectedFather = "";
                                    autoTV_villageCode.setText("");
                                    autoTV_growerCode.setText("");
                                    byteArray_photo_1 = null;
                                    byteArray_photo_2 = null;
                                    byteArraySignature = null;
                                    byteArrayThumb = null;
                                    getGoToNextActivty();
                                }
                            }
                            else
                            {
                                new AlertDialogManager().showToast(AgriFormActivity.this, "Thumb not found");
                            }
                        } else {
                            new AlertDialogManager().showToast(AgriFormActivity.this, "Signature not found");
                        }
                    } else {
                        new AlertDialogManager().showToast(AgriFormActivity.this, "Please select photo 2");
                    }
                } else {
                    new AlertDialogManager().showToast(AgriFormActivity.this, "Please select photo 1");
                }
            } else {
                new AlertDialogManager().showToast(AgriFormActivity.this, getResources().getString(R.string.enter_grower));
            }
        } else {
            new AlertDialogManager().showToast(AgriFormActivity.this, getResources().getString(R.string.enter_village));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.USE_BIOMETRIC, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.USE_BIOMETRIC) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "CAMERA & phone  permission granted");
                        doPermissionGranted();
                    } else {
                        Log.d(TAG, "Camera and phone Permission are not granted ask again ");
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.USE_BIOMETRIC)) {
                            showDialogOK("Camera and phone Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    UtilFile.checkRequestPermiss(getApplicationContext(), AgriFormActivity.this);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            explain("Go to settings and enable permissions");
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void explain(String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("com.vibrant.agriinput")));
                        // startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //for gallery Image 1
        try {
            if (requestCode == PERMISSION_REQUEST_CODE_1 && resultCode == RESULT_OK) {
                //for camera Image 1
                if (data != null && data.getExtras() != null) {
                    Bitmap photo_Bitmap_1 = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo_Bitmap_1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray_photo_1 = stream.toByteArray();
                    ivPhoto_1.setImageBitmap(photo_Bitmap_1);
                    dialog_1.dismiss();
                    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(photo_Bitmap_1);
                    detectFaces(image);
                }
            } else if (requestCode == PERMISSION_REQUEST_CODE_2 && resultCode == RESULT_OK) {
                //For Camera Image 2
                if (data != null && data.getExtras() != null) {
                    Bitmap photo_Bitmap_2 = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo_Bitmap_2.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray_photo_2 = stream.toByteArray();
                    ivPhoto_2.setImageBitmap(photo_Bitmap_2);
                    dialog_2.dismiss();
                }
            }
        }
        catch (Exception e)
        {
            Log.d("Error",e.toString());
        }
    }

    private void detectFaces(FirebaseVisionImage image) {
        try {
            FirebaseApp.initializeApp(AgriFormActivity.this);
            // [START set_detector_options]
            FirebaseVisionFaceDetectorOptions options =
                    new FirebaseVisionFaceDetectorOptions.Builder()
                            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                            .setMinFaceSize(0.15f)
                            .enableTracking()
                            .build();
            // [END set_detector_options]

            // [START get_detector]
            FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                    .getVisionFaceDetector(options);
            // [END get_detector]

            // [START fml_run_detector]
            Task<List<FirebaseVisionFace>> result =
                    detector.detectInImage(image)
                            .addOnSuccessListener(
                                    new OnSuccessListener<List<FirebaseVisionFace>>() {
                                        @Override
                                        public void onSuccess(List<FirebaseVisionFace> faces) {
                                            // Task completed successfully
                                            // [START_EXCLUDE]
                                            // [START get_face_info]
                                            if(faces.size()>0)
                                            {
                                                for (FirebaseVisionFace face : faces) {
                                                    Rect bounds = face.getBoundingBox();
                                                    float rotY = face.getHeadEulerAngleY();  // Head is rotated to the right rotY degrees
                                                    float rotZ = face.getHeadEulerAngleZ();  // Head is tilted sideways rotZ degrees

                                                    // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                                    // nose available):
                                                    /*FirebaseVisionFaceLandmark leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR);
                                                    if (leftEar != null) {
                                                        FirebaseVisionPoint leftEarPos = leftEar.getPosition();
                                                    }*/

                                                    // If classification was enabled:
                                                    /*if (face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                        float smileProb = face.getSmilingProbability();
                                                    }*/
                                                    /*if (face.getRightEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                        float rightEyeOpenProb = face.getRightEyeOpenProbability();
                                                    }*/

                                                    //face.g
                                                    if(face.getRightEyeOpenProbability()>0.9 && face.getLeftEyeOpenProbability() >0.9 )
                                                    {

                                                    }
                                                    else
                                                    {
                                                        ivPhoto_1.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_person_outline_24));
                                                        byteArray_photo_1 = null;
                                                        new AlertDialogManager().showToast(AgriFormActivity.this, "Sorry face not detected on captured image");
                                                    }
                                                    // If face tracking was enabled:
                                                    if (face.getTrackingId() != FirebaseVisionFace.INVALID_ID) {
                                                        int id = face.getTrackingId();
                                                    }
                                                }
                                            }
                                            else
                                            {
                                                ivPhoto_1.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_person_outline_24));
                                                byteArray_photo_1 = null;
                                                new AlertDialogManager().showToast(AgriFormActivity.this, "Sorry face not detected on captured image");
                                                //dialog_1.dismiss();
                                            }

                                            // [END get_face_info]
                                            // [END_EXCLUDE]
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                        }
                                    });
            // [END fml_run_detector]
        }
        catch (Exception e)
        {
            Log.d("","");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private long mLastAttTime=0l;
    @Override
    public void OnDeviceAttached(int vid, int pid, boolean hasPermission) {
        if (SystemClock.elapsedRealtime() - mLastAttTime < Threshold) {
            return;
        }
        mLastAttTime = SystemClock.elapsedRealtime();
        int ret;
        if (!hasPermission) {
            //SetTextOnUIThread("Permission denied");
            return;
        }
        try {
            if (vid == 1204 || vid == 11279) {
                if (pid == 34323) {
                    ret = mfs100.LoadFirmware();
                    if (ret != 0) {
                        //SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        //SetTextOnUIThread("Load firmware success");
                    }
                } else if (pid == 4101) {
                    String key = "Without Key";
                    ret = mfs100.Init();
                    if (ret == 0) {
                        //showSuccessLog(key);
                    } else {
                        //SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    long mLastDttTime=0l;
    @Override
    public void OnDeviceDetached() {
        try {

            if (SystemClock.elapsedRealtime() - mLastDttTime < Threshold) {
                return;
            }
            mLastDttTime = SystemClock.elapsedRealtime();
            UnInitScanner();

            //SetTextOnUIThread("Device removed");
        } catch (Exception e) {
        }
    }

    @Override
    public void OnHostCheckFailed(String s) {
        try {
            //SetLogOnUIThread(err);
            //Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }
    }

    private void UnInitScanner() {
        try {
            int ret = mfs100.UnInit();
            if (ret != 0) {
                //SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                //SetLogOnUIThread("Uninit Success");
                //SetTextOnUIThread("Uninit Success");
                lastCapFingerData = null;
            }
        } catch (Exception e) {
            Log.e("UnInitScanner.EX", e.toString());
        }
    }


    private void StartSyncCapture() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                //SetTextOnUIThread("");
                isCaptureRunning = true;
                try {
                    FingerData fingerData = new FingerData();
                    int ret = mfs100.AutoCapture(fingerData, timeout, false);
                    Log.e("StartSyncCapture.RET", "" + ret);
                    if (ret != 0) {
                        //SetTextOnUIThread(mfs100.GetErrorMsg(ret));
                    } else {
                        lastCapFingerData = fingerData;

                        final Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0,
                                fingerData.FingerImage().length);
                        AgriFormActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finger_icon.setImageBitmap(bitmap);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                byteArrayThumb = byteArrayOutputStream.toByteArray();

                                reset_thumb.setText("Clear Thumb");
                                reset_thumb.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //String uri = "@drawable/ic_fingerprint";  // where myresource (without the extension) is the file
                                        //int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                                        //Drawable res = getResources().getDrawable(imageResource);
                                        finger_icon.setImageDrawable(getDrawable(R.drawable.ic_baseline_fingerprint_24));
                                        reset_thumb.setText("Capture Thumb");
                                        reset_thumb.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                StartSyncCapture();
                                            }
                                        });
                                    }
                                });
                            }
                        });

//                        Log.e("RawImage", Base64.encodeToString(fingerData.RawData(), Base64.DEFAULT));
//                        Log.e("FingerISOTemplate", Base64.encodeToString(fingerData.ISOTemplate(), Base64.DEFAULT));
                        //SetTextOnUIThread("Capture Success");
                        /*String log = "\nQuality: " + fingerData.Quality()
                                + "\nNFIQ: " + fingerData.Nfiq()
                                + "\nWSQ Compress Ratio: "
                                + fingerData.WSQCompressRatio()
                                + "\nImage Dimensions (inch): "
                                + fingerData.InWidth() + "\" X "
                                + fingerData.InHeight() + "\""
                                + "\nImage Area (inch): " + fingerData.InArea()
                                + "\"" + "\nResolution (dpi/ppi): "
                                + fingerData.Resolution() + "\nGray Scale: "
                                + fingerData.GrayScale() + "\nBits Per Pixal: "
                                + fingerData.Bpp() + "\nWSQ Info: "
                                + fingerData.WSQInfo();
                        //SetLogOnUIThread(log);
                        SetData2(fingerData);*/
                    }
                } catch (Exception ex) {
                    //SetTextOnUIThread("Error");
                } finally {
                    isCaptureRunning = false;
                }
            }
        }).start();
    }

    private void StopCapture() {
        try {
            mfs100.StopAutoCapture();
        } catch (Exception e) {
            //SetTextOnUIThread("Error");
        }
    }

    private void InitScanner() {
        try {
            int ret = mfs100.Init();
            if (ret != 0) {
                //SetTextOnUIThread(mfs100.GetErrorMsg(ret));
            } else {
                //SetTextOnUIThread("Init success");
                String info = "Serial: " + mfs100.GetDeviceInfo().SerialNo()
                        + " Make: " + mfs100.GetDeviceInfo().Make()
                        + " Model: " + mfs100.GetDeviceInfo().Model()
                        + "\nCertificate: " + mfs100.GetCertification();
                //SetLogOnUIThread(info);
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Init failed, unhandled exception",
                    Toast.LENGTH_LONG).show();
            //SetTextOnUIThread("Init failed, unhandled exception");
        }
    }
}

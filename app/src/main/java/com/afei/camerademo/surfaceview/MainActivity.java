package com.afei.camerademo.surfaceview;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afei.camerademo.PermissionControl;
import com.afei.camerademo.R;
import com.afei.camerademo.camera.CameraControl;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    private AutoFitTextureView mTextureview;
    private Button mVideoRecodeBtn;//开始录像
    private LinearLayout mVerticalLinear;
    private CameraControl mCameraControl;
    private boolean mIsRecordingVideo; //录像状态记录
    public static String BASE_PATH = Environment.getExternalStorageDirectory() + "/AAA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Activity对象
        PermissionControl.getInstance().chekPermissions(this, permissions, permissionsResult);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 创建监听权限的接口对象
     */
    private PermissionControl.IPermissionsResult permissionsResult = new PermissionControl.IPermissionsResult() {
        @Override
        public void passPermissons() {
            //授权后的操作
            //获取相机管理类的实例
            mCameraControl = CameraControl.getInstance(MainActivity.this);
            mCameraControl.setFolderPath(BASE_PATH);

            initView();
        }

        @Override
        public void forbitPermissons() {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        PermissionControl.getInstance().onRequestPermissionsResult(this, requestCode,
                permissions, grantResults);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        mTextureview = (AutoFitTextureView) findViewById(R.id.textureview);
        mVideoRecodeBtn = (Button) findViewById(R.id.video_recode_btn);
        mVideoRecodeBtn.setOnClickListener(this);
        mVerticalLinear = (LinearLayout) findViewById(R.id.vertical_linear);
        //mVerticalLinear.setVisibility(View.GONE);
        mCameraControl.initCamera(mTextureview);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.video_recode_btn:
                if(mIsRecordingVideo){
                    mIsRecordingVideo = !mIsRecordingVideo;
                    mCameraControl.stopRecordingVideo();
                    mVideoRecodeBtn.setText("开始录像");
                    Toast.makeText(this,"录像结束",Toast.LENGTH_SHORT).show();
                }
                else{
                    mIsRecordingVideo = !mIsRecordingVideo;
                    mVideoRecodeBtn.setText("停止录像");
                    mCameraControl.startRecordingVideo();
                    Toast.makeText(this,"录像开始",Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }

    }
}

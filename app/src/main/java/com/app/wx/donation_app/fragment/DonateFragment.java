package com.app.wx.donation_app.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.wx.donation_app.R;
import com.app.wx.donation_app.activity.DonateSuccessActivity;
import com.app.wx.donation_app.config.Constant;
import com.app.wx.donation_app.entry.BasePojo;
import com.app.wx.donation_app.entry.ProjectDetail;
import com.app.wx.donation_app.entry.User;
import com.app.wx.donation_app.util.ImageUtil;
import com.app.wx.donation_app.util.SPUtil;
import com.app.wx.donation_app.util.VerifyPermission;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.longsh.optionframelibrary.OptionBottomDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.sunfusheng.glideimageview.GlideImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

/**
 * @Author:wx
 * @Date:2019/3/8
 */
public class DonateFragment extends BaseFragment implements View.OnClickListener {

    private static final int RESULT_SUCCESS = 0;     // 成功的结果码
    private static final int RESULT_FAIL = -1;       // 失败的结果码

    private static final int REQUEST_PHOTO_CAMERA = 0;    // 拍照请求
    private static final int REQUEST_PHOTO_ALBUM = 1;   // 读取本地相册请求
    private static final int RESULT_PHONE_CROP = 2;   // 返回裁图结果
    private static final String TAG = "捐赠页面";

    private EditText etProjectName,etProjectDescription,etBookName,etNeedNumber;
    private ImageView ivProject;
    private Button btnSubmission;
    private AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求

    private File tempFile;    // 拍照的照片临时存放路径
    private Uri imageUri;   // 拍照的照片返回数据格式

    File cutfile;           //截图文件
    private Uri mCutUri;

    String imgUrl = "http://img4.imgtn.bdimg.com/it/u=3202776041,1561290399&fm=26&gp=0.jpg";

    private String cutcameraNew;    //截图生成的文件uri

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donate, container, false);
        // 获取新建视图View中布局文件的空间
        initView(view);
        return view;
    }

    private void initView(View view) {
        etProjectName = view.findViewById(R.id.et_project_name);
        etProjectDescription = view.findViewById(R.id.et_project_description);
        etBookName = view.findViewById(R.id.et_book_name);
        etNeedNumber = view.findViewById(R.id.et_need_number);
        ivProject = view.findViewById(R.id.iv_project_donate);
        btnSubmission = view.findViewById(R.id.btn_submission);

        ivProject.setOnClickListener(this);
        btnSubmission.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submission:
                if (!SPUtil.isLogin(getContext())){
                    Toast.makeText(getContext(), "你还未登录，请先登录", Toast.LENGTH_SHORT).show();
                }else {
                    ProjectDonate();
                    etProjectDescription.setText("");
                    etProjectName.setText("");
                    etBookName.setText("");
                    etNeedNumber.setText("");
                    ivProject.setImageResource(R.drawable.ic_picture_add);
                    /*发起成功，跳转到等待审核页面*/
                    Intent intent = new Intent(getContext(), DonateSuccessActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_project_donate:
                choosePicture();
        }

    }

    private void choosePicture() {
        /*两种选择方式*/
        List<String> stringList = new ArrayList<String>();
        stringList.add("拍照");
        stringList.add("从相册选择");
        /*申请权限*/
        VerifyPermission.verifyCameraPermission(getActivity());
        Log.i("PersonInfoActivity", "ChoseHeadDialog: 申请相机权限");
        /*底部弹出*/
        final OptionBottomDialog optionBottomDialog = new OptionBottomDialog(getContext(), stringList);

        optionBottomDialog.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                optionBottomDialog.dismiss();
                switch (position){
                    case 0:         //拍照
                        takePhoto();    // 选择相机
                        break;
                    case 1:         //从相册中选择
                        choosePhoto();  // 选择相册
                        break;
                }
            }

        });

    }

    /*通过拍照上传*/
    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调起相机
        /*判断存储卡是否可以用，可用进行存储*/
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String filename = "myCamera"+ ImageUtil.getTempFileName() + ".png";
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    filename);     // 指定图片文件名
            imageUri = Uri.fromFile(tempFile);   // 从文件中创建uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 指定拍照返回的uri
        }
        startActivityForResult(intent, REQUEST_PHOTO_CAMERA);
    }

    /*通过从相册选择图片上传*/
    private void choosePhoto(){
        /* 打开选择图片的界面*/
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_PHOTO_ALBUM);
    }

    /**
     * 处理拍照或者从相册选择相片的回调函数
     * @param req  ：请求码
     * @param res  ：结果码
     * @param data :返回数据
     */
    @Override
    public void onActivityResult(int req, int res, Intent data) {
        if (res == RESULT_OK ){

            switch (req){
                case REQUEST_PHOTO_CAMERA:      //拍照请求回调

                    Log.i("PersonInfoActivity", "onActivityResult: 拍照返回之后1");
                    startActivityForResult(CutForPhoto(imageUri),RESULT_PHONE_CROP);
                    break;

                case REQUEST_PHOTO_ALBUM:       //相册请求回调
                    Uri uri = data.getData();    // 该uri是上一个Activity返回的
                    Log.i("PersonInfoActivity", "onActivityResult: 从相册选择图片");
                    startActivityForResult(CutForPhoto(uri),RESULT_PHONE_CROP);  //图片裁剪
                    break;

                case RESULT_PHONE_CROP:         //截图请求回调
                    photoDel(tempFile);   //删除拍照的临时文件

                    try {
                        Log.i("PersonInfoActivity", "onActivityResult: 返回成功后的图片");
                        Uri qiniuUri = mCutUri;
                        InputStream qiniuImgIs = null;
                        qiniuImgIs = getContext().getContentResolver().openInputStream(qiniuUri);
                        Bitmap qiniuBitmap = BitmapFactory.decodeStream(qiniuImgIs);
                        headUpload(qiniuBitmap,cutcameraNew); //上传到七牛云
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.i("PersonInfoActivity", "onActivityResult: 截取失败");
                    }

                    break;
            }

        }
    }

    /**
     * 图片裁剪
     * @param uri 图片url路径
     * @return    返回数据在intent数据中
     */
    private Intent CutForPhoto(Uri uri) {
        try {
            //直接裁剪
            Intent intent = new Intent("com.android.camera.action.CROP");
            //设置裁剪之后的图片路径文件

            cutcameraNew = "image_"+ ImageUtil.getTempFileName()+".png";  //文件名
            cutfile = new File(Environment.getExternalStorageDirectory().getPath(),
                    cutcameraNew); //生成的截图文件

            photoDel(cutfile);   //若存在本地图片，则删除

            cutfile.createNewFile();
            //初始化 uri
            Uri imageUris = uri; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            Log.d("PersonInfoActivity", "CutForPhoto: "+cutfile);
            outputUri = Uri.fromFile(cutfile);
            mCutUri = outputUri;
            Log.d("PersonInfoActivity", "mCameraUri: "+mCutUri);
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop",true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX",1);
            intent.putExtra("aspectY",1);
            //设置要裁剪的宽高
            intent.putExtra("outputX", dip2px(200)); //200dp
            intent.putExtra("outputY", dip2px(200));
            intent.putExtra("scale",true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data",false);
            if (imageUris != null) {
                intent.setDataAndType(imageUris, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            return intent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*把dip转换成px*/
    public  float dip2px(float dipValue)
    {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return  (dipValue * scale + 0.5f);
    }

    /**
     * 删除本地生成的临时图片
     */
    private void photoDel(File tempFile) {
        if( tempFile == null || !tempFile.exists() ){return;} //文件不存在，直接返回

        if (tempFile.delete()) {       //删除临时照片
            Log.i("PersonInfoActivity", "takePhoto: 照片已删除");
            Log.i(TAG, "photoDel: imageUri："+imageUri);
        }else{
            Log.i("PersonInfoActivity", "takePhoto: 照片未删除");
            Log.i(TAG, "photoDel: imageUri："+imageUri);
        }

    }

    /**
     * 上传图片到七牛云
     * @return
     */
    private void headUpload(final Bitmap qiniuBitmap, final String fileName){
        RequestParams params=new RequestParams();     //封装参数
        params.put("path",fileName);
        httpClient.post(Constant.QINIU_UPTOKEN, params ,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "Success: 连接服务器成功！");
                if(statusCode==200){        //响应成功

                    String jsonString = new String(responseBody);
                    if(jsonString != null){        //不为空，已获取upToken
                        // 将Bitmap转换为OutputStream对象
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        qiniuBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        UploadManager uploadManager = new UploadManager();
                        // 参数1 ： 上传的图片，byte[]格式
                        // 参数2 ：自定义图片名，必须唯一，可以使用时间戳
                        // 参数3 ： 上传的凭证，从后台获取
                        // 参数4 ：上传后的回调方法，如果上传成功七牛会将图片的完整路径返回
                        uploadManager.put(baos.toByteArray(), fileName, jsonString, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, JSONObject response) {
                                if (info.statusCode == 200) {
                                    String urlKey = "";
                                    try {
                                        urlKey = response.getString("key");
                                        // 图片上传成功后，将用户修改的信息封装在Customer对象，发送到服务端执行个人信息修改操作
                                        Log.i("上传成功提示", "上传成功");

                                        photoDel(cutfile);    //删除截图所生成的文件
                                        imgUrl = Constant.QINIU_BASE_URL + urlKey;
                                        Glide.with(getActivity()).load(imgUrl).into(ivProject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.i("上传错误提示", "complete: "+info.error);

                                }
                            }
                        }, null);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(TAG, "onFailure: 连接服务器失败！"+Constant.QINIU_UPTOKEN);
                Log.i(TAG, "statusCode: "+statusCode);
                Log.i(TAG, "headers: "+headers);
                Log.i(TAG, "responseBody: "+responseBody);
                error.printStackTrace();
            }
        });
    }



    public void ProjectDonate() {
        String projectName = etProjectName.getText().toString();
        final String projectDescription = etProjectDescription.getText().toString();
        String bookName = etBookName.getText().toString();
        int needNum = 10 ;
        if (etNeedNumber.getText().toString()!= ""||etNeedNumber.getText().toString()!=null){
            needNum = Integer.parseInt(etNeedNumber.getText().toString());
        }
        if (projectName==null || projectDescription==null || bookName==null || (Integer)needNum == null || imgUrl == null){
            Toast.makeText(getContext(),"必填项不能为空",Toast.LENGTH_LONG).show();
        }else {
            ProjectDetail projectDetail = new ProjectDetail(projectName, bookName, needNum, imgUrl, projectDescription);
            User user = SPUtil.getUser(getContext());
            projectDetail.setStartUserId(user.getUserId());

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .create();
            String projectJson = gson.toJson(projectDetail);
            Log.i(TAG, "json字符串" + projectJson);
            RequestParams requestParams = new RequestParams();
            requestParams.put("projectJson", projectJson);
            httpClient.post(Constant.URL_USER_START_PROJECT, requestParams, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.i(TAG, "Success: 连接服务器成功！");
                    //响应成功
                    if (statusCode == 200) {
                        String jsonString = new String(responseBody);
                        BasePojo basePojo = new Gson().
                                fromJson(jsonString, new TypeToken<BasePojo<ProjectDetail>>() {
                                }.getType());
                        if (basePojo.isSuccess()) {
                            Log.i(TAG, "onSuccess: 发起成功");
                            ProjectDetail project = (ProjectDetail) basePojo.getData().get(0);
                            Log.i(TAG, "onSuccess: projectDetail" + project);
                            Toast.makeText(getContext(), basePojo.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), basePojo.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i(TAG, "onFailure: 连接服务器失败！");
                    Log.i(TAG, "statusCode: " + statusCode);
                    Log.i(TAG, "headers: " + headers);
                    Log.i(TAG, "responseBody: " + responseBody);
                    Log.i(TAG, "error: " + error);
                }
            });
        }
    }
}

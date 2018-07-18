package com.example.csy.activitypractice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpeechRecognitionActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.iat_text)
    EditText iatText;
    @BindView(R.id.iat_recognize)
    Button iatRecognize;
    @BindView(R.id.iat_stop)
    Button iatStop;
    @BindView(R.id.iat_cancel)
    Button iatCancel;
    @BindView(R.id.parent)
    LinearLayout parent;
    private ImageView btnClose;
    private TextView tvTips;
    private LinearLayout llySpeak;
    private ImageView imgSpeak;

    private WaveLineView waveLineView;
    private PopupWindow popWnd = null;
    private View contentView = null;
    private static String TAG = "CSYLALA";
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private Toast mToast;

    private int ret = 0;
    private static final int AUDIO_RECORD = 0;//申请打电话权限的请求码,≥0即可


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recognition);
        ButterKnife.bind(this);
        initLayout();
        requestPermissions();

    }

    private void requestPermissions() {
        //检测是否有拨打电话的权限
        int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_RECORD);//动态申请打电话权限
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AUDIO_RECORD:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    Toast.makeText(this, "用户未允许打电话权限", Toast.LENGTH_SHORT).show();
                    boolean b = shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE);
                    if(!b) {
                        //用户没同意授权,还不让下次继续提醒授权了,这是比较糟糕的情况
                        Toast.makeText(this,"用户勾选了下次不再提醒选项", Toast.LENGTH_SHORT).show();
                    }
                }
            default:
                break;
        }
    }

    private void init() {
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(SpeechRecognitionActivity.this, mInitListener);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    /**
     * 初始化Layout。
     */
    private void initLayout() {
        iatRecognize.setOnClickListener(this);
        iatStop.setOnClickListener(this);
        iatCancel.setOnClickListener(this);
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            showTip(error.getPlainDescription(true));
            if (error.getErrorCode() == 10118) {
                noContent();
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            printResult(results);
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            waveLineView.setVolume(30 + (int) (volume * 2.5));
            waveLineView.setMoveSpeed((float) (290 - volume * 0.5));
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    //没有说话
    private void noContent() {

        if (waveLineView.isRunning()) {
            waveLineView.stopAnim();
        }
        tvTips.setText("好像没有听清您说话呢");
        imgSpeak.setVisibility(View.VISIBLE);
        llySpeak.setVisibility(View.VISIBLE);
    }


    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIatResults.put(sn, text);
        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        if (results != null && resultBuffer.toString().length() != 0) {
            iatText.setText(resultBuffer.toString());
            iatText.setSelection(iatText.length());
            closePopWindow();
        } else {
            noContent();
        }
    }

    @Override
    public void onClick(View view) {
        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }
        switch (view.getId()) {
            // 开始听写
            case R.id.iat_recognize:
                startListen();
                break;
            // 停止听写
            case R.id.iat_stop:
                mIat.stopListening();
                showTip("停止听写");
                break;
            // 取消听写
            case R.id.iat_cancel:
                mIat.cancel();
                showTip("取消听写");
                break;
            default:
                break;
        }
    }

    private void startListen() {
        showPopupWindow();
        // 移动数据分析，收集开始听写事件
        FlowerCollector.onEvent(SpeechRecognitionActivity.this, "iat_recognize");
        iatText.setText(null);// 清空显示内容
        tvTips.setText("请输入内容");
        mIatResults.clear();
        // 设置参数
        setParam();
        // 不显示听写对话框
        ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            showTip("听写失败,错误码：" + ret);
        } else {
            showTip("开始了");
        }
    }

    private void showPopupWindow() {
        if (popWnd == null) {
            if (contentView == null) {
                contentView = LayoutInflater.from(SpeechRecognitionActivity.this).inflate(R.layout.popup_window_speak, null);
            }
            popWnd = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, 600);
            popWnd.setContentView(contentView);
            popWnd.setOutsideTouchable(true);
            popWnd.setFocusable(true);
            popWnd.setOutsideTouchable(true);
            waveLineView = contentView.findViewById(R.id.waveLineView);
            btnClose = contentView.findViewById(R.id.btn_close);
            llySpeak = contentView.findViewById(R.id.lly_speak);
            imgSpeak = contentView.findViewById(R.id.img_speak);
            tvTips = contentView.findViewById(R.id.tv_tips);
        }
        popWnd.setAnimationStyle(R.style.mypopwindow_anim_style);
        popWnd.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

        if (waveLineView.isRunning()) {
            waveLineView.stopAnim();
        }
        waveLineView.startAnim();

        llySpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startListen();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closePopWindow();
            }
        });
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                closePopWindow();
            }
        });
    }

    private void closePopWindow() {
        imgSpeak.setVisibility(View.GONE);
        llySpeak.setVisibility(View.GONE);
        popWnd.dismiss();
        waveLineView.stopAnim();
        mIat.stopListening();//取消听写
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        String lag = "mandarin";
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, lag);

        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    @Override
    protected void onResume() {

        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(SpeechRecognitionActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
        if (waveLineView != null) {
            waveLineView.onResume();
        }
    }

    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(SpeechRecognitionActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onPause();
        if (waveLineView != null) {
            waveLineView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (waveLineView != null) {
            waveLineView.release();
        }
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }
}

package net.shenru.xixi.app;

import java.io.InputStream;

import net.shenru.xixi.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;

public class Weather implements SynthesizerPlayerListener {

	private Context mContext;
	private View mView;
	private WebView mWebView;
	private View mCloseView;
	private boolean isShow = false;
	private WindowManager mWindowManager;

	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};
	/**
	 * @param context
	 * @param str
	 */
	public Weather(Context context, String str) {
		// http://www.cnblogs.com/iphone520/archive/2011/12/24/2300086.html
		this.mContext = context;
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mView = View.inflate(context, R.layout.weather_fragment, null);
		mWebView = (WebView) mView.findViewById(R.id.wv);
		mCloseView = mView.findViewById(R.id.close);
		mCloseView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Button bt = (Button) v;
				if("关闭".equals(bt.getText().toString().trim())){
					release();
				}else{
				}
			}
		});
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginsEnabled(true);

		mWebView.loadUrl("http://www.hao123.com/tianqi");
		// mWebView.pageDown(true);
		mWebView.scrollTo(200, 200);
		mWebView.pageDown(true);
	}

	public void showView() {
		if (isShow) {
			return;
		}
		isShow = true;

		WindowManager.LayoutParams p = new WindowManager.LayoutParams();
		p.gravity = Gravity.CENTER;
		p.width = -1;
		p.height = -2;

		p.format = PixelFormat.RGBA_8888;
		p.type = WindowManager.LayoutParams.TYPE_PHONE;
		p.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		// p.format = PixelFormat.TRANSLUCENT;
		// p.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// p.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT |
		// WindowManager.LayoutParams.TYPE_TOAST;
		p.setTitle("PopupWindow:");

		mWindowManager.addView(mView, p);

		player();
	}

	public void release() {
		if (mView != null) {
			mWindowManager.removeView(mView);
			mView = null;
		}
	}

	private void player() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		try {
			HttpClient client = new DefaultHttpClient();

			HttpGet httpGet = new HttpGet("http://m.weather.com.cn/data/101010100.html");

			HttpResponse execute = client.execute(httpGet);
			HttpEntity entity = execute.getEntity();
			if (entity != null) {
				InputStream content = entity.getContent();

				byte buf[] = new byte[1024];
				int len = -1;

				while ((len = content.read(buf)) != -1) {
					sb.append(new String(buf, 0, len));
				}

				// JSONTokener tokener = new
				// JSONTokener(sb.toString());
				JSONObject jsonObj = new JSONObject(sb.toString()).getJSONObject("weatherinfo");
				// 基本信息
				String city = jsonObj.getString("city");
				// 日期
				String date = jsonObj.getString("date_y");
				String week = jsonObj.getString("week");
				// 天气描述
				String weather = jsonObj.getString("weather1");
				// 温度
				String degrees = jsonObj.getString("temp1");
				// 风力
				String wind = jsonObj.getString("wind1");
				// 概括
				String index = jsonObj.getString("index");
				// 描述
				String des = jsonObj.getString("index_d");
				// 洗车 "index_xc":"适宜",
				String carCleaning = jsonObj.getString("index_xc");
				// 旅游 "index_tr":"较适宜",
				String tourism = jsonObj.getString("index_tr");
				// 舒适指数 "index_co":"较不舒适",
				String comfortableIndex = jsonObj.getString("index_co");
				// 晨练 "index_cl":"适宜",
				String exercise = jsonObj.getString("index_cl");
				// 晾晒 "index_ls":"适宜",
				String airCure = jsonObj.getString("index_ls");
				// 过敏 "index_ag":"极易发"}}
				String allergy = jsonObj.getString("index_ag");

				// 基本信息 摄氏温度 天气描述 风速描述 今天穿衣指数
				String subStr = "天气预报  " + city + "   " + date + week + "   " + weather + "   "
						+ degrees + "   " + wind + "   " + index + "   今天穿衣指数         " + des + "    洗车            "
						+ carCleaning + "    旅游     " + tourism + "   晨练            " + exercise
						+ "        舒适指数             " + comfortableIndex + "   晾晒             " + airCure;

				SynthesizerPlayer player = SynthesizerPlayer.createSynthesizerPlayer(mContext, "appid="
						+ mContext.getString(R.string.iflytek_key));
				player.setVoiceName("vixq");
				player.setSpeed(40);
				player.setVolume(90);
				player.playText(subStr, null, this);
				System.out.println(subStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mCloseView.performClick();
		}
	}

	@Override
	public void onBufferPercent(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnd(SpeechError arg0) {
		mCloseView.performClick();
	}

	@Override
	public void onPlayBegin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayPaused() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayPercent(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayResumed() {
		// TODO Auto-generated method stub

	}
}

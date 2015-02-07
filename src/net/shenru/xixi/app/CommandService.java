package net.shenru.xixi.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

/**
 * 执行命令
 */
public class CommandService extends Service {

	private CommandBroadCastReceiver commandBroad;

	@Override
	public void onCreate() {
		super.onCreate();

		IntentFilter intentFilter = new IntentFilter("com.shenru.command");
		commandBroad = new CommandBroadCastReceiver();
		registerReceiver(commandBroad, intentFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class CommandBroadCastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ("com.shenru.command".equals(intent.getAction())) {
				int intExtra = intent.getIntExtra("error", 999999990);
				if (intExtra != 999999990) {
					// 有错误
					Toast.makeText(getApplicationContext(), "error:" + intExtra, 0).show();
				} else {
					// execute 识别
					// shell 执行 
					// data 数据
					String executeData = intent.getStringExtra("execute");
					String shellData = intent.getStringExtra("shell");

					if (executeData != null) {
						Toast.makeText(getApplicationContext(), "execute :" + executeData, 0)
								.show();
						if (isWeatherForecast(executeData)) {
							AppAction.weatherForecast(context, executeData);
						} else if (isPlayMusic(executeData)) {
							// 打开音乐播放器
							AppAction.playMusic(context, executeData);
						} else if (isCallTelephone(executeData)) {
							// 打电话
						}
					}
					if (shellData != null) {
						// TODO
						Toast.makeText(getApplicationContext(), "shell :" + shellData, 0)
								.show();
					}
				}
			}
		}

		// 是否播打电话
		private boolean isCallTelephone(String str) {
			// Pattern pattern = Pattern.compile(".*打电话.*[0-9].*");
			// Matcher matcher = pattern.matcher(str);
			// return matcher.matches();
			String p[] = { "打电话", "拨打电话" };
			for (String s : p) {
				if (s.equalsIgnoreCase(str)) {
					return true;
				}
			}
			return false;
		}

		// 天气预报
		private boolean isWeatherForecast(String str) {
			String p[] = { "天气预报", "预报天气" };
			for (String s : p) {
				if (s.equalsIgnoreCase(str)) {
					return true;
				}
			}
			return false;
		}

		private boolean isPlayMusic(String str) {
			String p[] = { "音乐播放器", "打开音乐", "播放音乐", "打开播放器", "打开音乐播放器" };
			for (String s : p) {
				if (s.equalsIgnoreCase(str)) {
					return true;
				}
			}
			return false;
		}
	}
}

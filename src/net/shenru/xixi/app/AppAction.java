package net.shenru.xixi.app;

import android.content.Context;

public class AppAction {

	private static PlayMusicer playerMusic;
	private static Weather mWeather;

	public static void weatherForecast(Context context, String name) {
		for (int i = 0; i < 2; i++) {
			if (mWeather == null) {
				mWeather = new Weather(context, name);
				mWeather.showView();
				break;
			} else {
				mWeather.release();
				mWeather = null;
			}
		}
	}

	public static void playMusic(Context context, String name) {
		for (int i = 0; i < 2; i++) {
			if (playerMusic == null) {
				playerMusic = new PlayMusicer(context);
				playerMusic.showView();
				break;
			} else {
				playerMusic.release();
				playerMusic = null;
			}
		}
		// if ("打开".equals(name)) {
		// playerMusic.showView();
		// } else if ("关闭".equals(name)) {
		// playerMusic.release();
		// playerMusic = null;
		// }
	}
}

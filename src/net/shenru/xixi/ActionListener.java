package net.shenru.xixi;

/**
 * @author 事件触发器
 */
public interface ActionListener {
	void startLook(String clazz);
	void stopLook(String clazz);
	void reply(String clazz);
}

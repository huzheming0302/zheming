package com.goods.application;

import com.goods.process.GoodsProcess;

import exhi.net.interface1.INetConfig;
import exhi.net.log.NetLog;
import exhi.net.netty.NetApplication;
import exhi.net.netty.NetProcess;



public class GoodsApplication extends NetApplication {
	@Override
	public INetConfig onGetConfig() {
		return GoodsConfig.instance();
	}

	@Override
	public NetProcess onGetProcess() {
		return new GoodsProcess();
	}

	public static void main(String[] args) {
		GoodsApplication app = new GoodsApplication();

		app.setDebugMode(true);
		GoodsConfig.instance().readConfig();
		
		NetLog.info("Application", GoodsApplication.class.getName());
		
		app.run(args, "30100D54B46B0CF6CCE7DDEF490C7988");
	}


}

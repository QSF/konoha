package application;

import router.Router;

public class Registry {
	
	private static Registry instance = new Registry();
	
	private Router router;
	
	private Registry(){};
	
	public static Registry getInstance() {
		return instance;
	}

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}
	
}

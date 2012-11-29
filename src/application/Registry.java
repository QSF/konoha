package application;

import gui.KonohaWindow;
import gui.NeighborsWindow;
import gui.UploadWindow;
import router.Router;

public class Registry {
	
	private static Registry instance = new Registry();
	
	private Router router;
	private P2PApplication p2pApplication;
	private KonohaWindow window;
	private NeighborsWindow neighborsWindow;
	private UploadWindow uploadWindow;
	
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

	public P2PApplication getP2pApplication() {
		return p2pApplication;
	}

	public void setP2pApplication(P2PApplication p2pApplication) {
		this.p2pApplication = p2pApplication;
	}

	public KonohaWindow getWindow() {
		return window;
	}

	public void setWindow(KonohaWindow window) {
		this.window = window;
	}

	public NeighborsWindow getNeighborsWindow() {
		return neighborsWindow;
	}

	public void setNeighborsWindow(NeighborsWindow neighborsWindow) {
		this.neighborsWindow = neighborsWindow;
	}

	public UploadWindow getUploadWindow() {
		return uploadWindow;
	}

	public void setUploadWindow(UploadWindow uploadWindow) {
		this.uploadWindow = uploadWindow;
	}
	
}

package application;

import gui.KonohaWindow;
import router.Router;

public class ApplicationStart {

	public static void main(String[] args) {
		
		Router router = new Router();
		Registry.getInstance().setRouter(router);
		
		P2PApplication p2pApplication = new P2PApplication();
		Registry.getInstance().setP2pApplication(p2pApplication);
		
		KonohaWindow window = new KonohaWindow();
		Registry.getInstance().setWindow(window);
	}

}

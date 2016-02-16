package app;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.LinkedList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import bowlingPoints.BowlingCalc;
import bowlingPoints.RulesException;

public class App {

	public static void main(String[] args) throws Exception {
		HttpServer server = HttpServer.create(new InetSocketAddress(8182), 0);
		server.createContext("/calc", new MyHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	static class MyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			
			String response;
			try {
				Integer points;
				points = getAnswer(t.getRequestURI().getQuery());
				response = points.toString();
			} catch (RulesException e) {
				response = e.getMessage();
			}
			
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();

			os.write(response.getBytes());
			os.close();
		}
		
		private static Integer getAnswer(String query) throws RulesException {
			LinkedList<String> frames = queryToList(query);
			
			BowlingCalc calc = new BowlingCalc();
			int[][] kicks = new int[frames.size()][];
			
			int i = 0;
			
			for (String s: frames) {
				String bowls[] = s.split(",");
				kicks[i] = new int[bowls.length];
				int ii = 0;
				for(String bowlStr: bowls) {
					try {
						kicks[i][ii++] = (int)Integer.parseInt(bowlStr);		
					} catch (Exception e) {
						throw new RulesException("Number must be number"); // показывает текст эксепшна под таблицей, 
												   //исходя из него приходится гадать, что
												   //именно не так введено.
					}
					
				}
				i++;
			}
			
			System.out.println(frames);
			
			return new Integer(calc.calcPoints(kicks));
		}

		/**
		 * returns the list of frame-bowls
		 * 
		 * @param query
		 * @return map
		 */
		private static LinkedList<String> queryToList(String query) {
			LinkedList<String> result = new LinkedList<>();
			for (String param : query.split("&")) {
				String pair[] = param.split("=");
				if (pair.length > 1 && pair[0].equals("frame[]")) {
					result.add(pair[1]);
				}
			}
			return result;
		}
	}
}

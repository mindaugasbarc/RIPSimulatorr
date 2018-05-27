import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RoutingTable implements Cloneable{
	private List<String> destinationIP = new ArrayList<String>();
	private List<Integer> metric = new ArrayList<Integer>();
	private List<String> nextHopIP = new ArrayList<String>();
	private List<String> neighbors = new ArrayList<String>();
	private String subnetMask = "255.255.255.0";
	private Router curRouter;

	public RoutingTable(RoutingTable table){
		this.destinationIP = new ArrayList<String>(table.destinationIP);
		this.metric = new ArrayList<Integer>(table.metric);
		this.nextHopIP = new ArrayList<String>(table.nextHopIP);
		this.neighbors = new ArrayList<String>(table.neighbors);
		this.curRouter = table.curRouter;
		
	}
	public RoutingTable(Router r){
		curRouter = r;
		addRecord( curRouter.getAddress(), 0, "0.0.0.0");
	}
	public void setDestinationIP(List<String> destIPs){
		destinationIP = destIPs;
	}
	public void setMetrics(List<Integer> metrics){
		metric = metrics;
	}
	public void setNextHops(List<String> hops){
		nextHopIP = hops;
	}
	public void setNeighbors(List<String> neighborsList){
		neighbors = neighborsList;
	}
	public void addRecord( String destination, int r_metric, String nextrouter){
		destinationIP.add(destination);
		metric.add(r_metric);
		nextHopIP.add(nextrouter);
	}
	public void removeRecord(int index){
		destinationIP.remove(index);
		metric.remove(index);
		nextHopIP.remove(index);
	}
	public String toString(){
		String s = "Destination\tNext hop\tMetric\n";
		for(int i = 0; i < destinationIP.size(); i++){
			s += destinationIP.get(i) + "\t" + nextHopIP.get(i) +  "\t" + metric.get(i) + "\n";
		}
		return s;
	}
	public void addNeighbor(String ip){
		neighbors.add(ip);
	}
	public List<String> getNeighbors(){
		return neighbors;
	}
	public List<String> getDestinations(){
		return destinationIP;
	}
	public List<Integer> getMetric(){
		return metric;
	}
	public List<String> getNextHopIP(){
		return nextHopIP;
	}
	public void update(Router router, List<Router> network){
		curRouter = router;
		router.setSentUpdate(false);
		
		Set<Edge> neighborsEdges = curRouter.getEdges();
		for(Edge curEdge : neighborsEdges){
			String srcAdress = curEdge.getSrc();
			String destAdress = curEdge.getDest();
			
			String neighbor;
			if(router.getAddress().equals(srcAdress)){
				neighbor = destAdress;
			}else{
				neighbor = srcAdress;
			}
			
			for(Router r : network){
				if(r.getAddress().equals(neighbor)){
					router.setSentUpdate(true);
					r.receive(curRouter.getAddress(), this, network);
					break;
				}
			}
		}
	}
	protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}


import java.util.LinkedList;
import java.util.Queue;

public class FlowNetwork implements FordFulkerson {
	// do not modify the next six lines
    private int[][] graph;
    private int[][] currentResidualGraph;
    private int[][] latestAugmentingPath;
    private int[][] currentFlow;
    private int[] parent;
    private int nRows;
    private int nCols;

    FlowNetwork(int[][] graph) {
        nRows = graph.length;
        if (nRows > 0) {			
			this.graph = graph;
            nCols = graph[0].length;
            if (nCols > 0) {
                currentResidualGraph = new int[nRows][nCols];
                for (int row = 0; row < nRows; row++) {
                    currentResidualGraph[row] = graph[row].clone();
                }

                latestAugmentingPath = new int[nRows][nCols];
                currentFlow = new int[nRows][nCols];
            }
        }
        //create a parent array with the size of the graph
        parent = new int[getGraph().length];
    }

    @Override
    public int step(int source, int sink) {
        // TODO
        int u,v;
        int pathFlow = 0;

        //reset the latestAugmentingPath array
        for (int row = 0; row < latestAugmentingPath.length; row++) {
            for (int col = 0; col < latestAugmentingPath[row].length; col++){
                latestAugmentingPath[row][col] = 0;
            }
        }

        if(BFSSearch(source, sink)){
            pathFlow = Integer.MAX_VALUE;
            pathFlow = singleFlow(source,sink,pathFlow);

        }
		return pathFlow;
    }

    @Override
    public int execute(int source, int sink){
        int maxFlow = 0;

        while(BFSSearch(source, sink)){
            int curFlow = step(source, sink);
            maxFlow += curFlow;
        }
        return maxFlow;
    }

    @Override
    public int[][] getGraph() {
        return graph;
    }

    @Override
    public int[][] getCurrentResidualGraph() {
        return currentResidualGraph;
    }

    @Override
    public int[][] getLatestAugmentingPath() {
        return latestAugmentingPath;
    }

    @Override
    public int[][] getCurrentFlow() {
        return currentFlow;
    }

    /**************************************
     * private auxiliary methods *
     *************************************/
	 
	 // TODO you may add further helper methods such as BFS search
    private boolean BFSSearch(int source, int sink){
        int size = getGraph().length;
        boolean visited[] = new boolean[size];

        //set every cell on false -> not visited
        for(int i = 0; i < size; i++){
            visited[i] = false;
        }
        //begin with the source
        LinkedList<Integer> path = new LinkedList<>();
        parent[source] = -1;
        path.add(source);
        visited[source] = true;

        //find path between source and sink
        while(path.isEmpty() == false) {
            int u = path.poll();

            for (int v = 0; v < size; v++) {
                if (visited[v] == false && currentResidualGraph[u][v] > 0) {
                    //vertex found path end -> return true
                    if (v == sink){
                        parent[v] = u;
                        return true;
                    }
                    path.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }
        //no path from source to sink found
        return false;
    }

    private int singleFlow(int source, int sink, int curFlow){
        int u,v;
        //choose the minimum flow of the path
        for (v = sink; v != source; v = parent[v]) {
            u = parent[v];
            curFlow = Math.min(curFlow, currentResidualGraph[u][v]);
        }

        // update the current residual graph with the minimum flow
        //update the latest augmenting path
        //update the current flow
        for (v = sink; v != source; v = parent[v]){
            u = parent[v];
            latestAugmentingPath[u][v] = curFlow;
            currentResidualGraph[u][v] -= curFlow;
            currentFlow[u][v] += curFlow;
        }
        return curFlow;
    }
}

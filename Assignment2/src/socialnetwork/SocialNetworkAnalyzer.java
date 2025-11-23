package socialnetwork;

import java.util.List;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SocialNetworkAnalyzer {
	
	private HashMap<String, List<String>> graph;
	private HashMap<String, List<String>> reverseGraph;
	private HashMap<String, Integer> influencerScores;

    /**
     * Constructor for the SocialNetworkAnalyzer. It takes the network data
     * and builds the internal graph representation.
     * @param followsGraph A map where the key is a user ID and the value is a list
     * of user IDs that the key user follows.
     */
	
    public SocialNetworkAnalyzer(Map<String, List<String>> followsGraph) {
        graph = new HashMap<>();
        reverseGraph = new HashMap<>();
        influencerScores = new HashMap<>();
        
        for(String follower: followsGraph.keySet()) {
            // Step 1: Copies follows graph to internal graph
        	graph.put(follower, new ArrayList<>(followsGraph.get(follower)));
            // Step 2: Reverses internal graph and puts it into reversegraph
        	for(String influencer: followsGraph.get(follower)) {
        		reverseGraph.putIfAbsent(influencer, new ArrayList<>());
        		reverseGraph.get(influencer).add(follower);        		
        	}
        }
        //Step 3: Calculating the influence scores of each influencer
        for(String influencer: reverseGraph.keySet()) {
        	influencerScores.put(influencer, reverseGraph.get(influencer).size());
        }
        
     // After populating graph and reverseGraph
        for (String follower : followsGraph.keySet()) {
            for (String influencer : followsGraph.get(follower)) {
                graph.putIfAbsent(influencer, new ArrayList<>());  // ensure all users exist in graph
                reverseGraph.putIfAbsent(follower, new ArrayList<>()); // ensure all users exist in reverseGraph
            }
        }
                
    }

    /**
     * Finds all the cliques in the social network.
     * A clique is a group of 2 or more users who are all mutually connected.
     * @return A list of sets, where each set represents a clique of user IDs.
     */
    public List<Set<String>> findCliques() {
    	List<Set<String>> cliques = new ArrayList<>();
    	Set<String> visited = new HashSet<>(); // I'm using a hashset to mark nodes visited.
    	Deque<String> finished = new ArrayDeque<>(); // Using a Deque as the stack.
    	
    	// Step 1: DFS on graph
    	for(String user : graph.keySet()) {
    		if(!visited.contains(user)) {
    			dfsGraph(user, visited, finished);
    		}
    	}
    	
    	// Step 2: DFS on reversegraph by popping from a stack of finishing times in decreasing order
    	visited.clear();
		while(!finished.isEmpty()) {
			String user = finished.pop();
			if(!visited.contains(user)) {
				Set<String> scc = new HashSet<>();
				dfsReverse(user, visited, scc);
				if(scc.size() >= 2) {
					cliques.add(scc);
				}
			}
		}
		
		return cliques;
    }

    // Helper method for findCliques()
    private void dfsReverse(String user, Set<String> visited, Set<String> scc) {
        visited.add(user);
        scc.add(user);
        
        List<String> followers = reverseGraph.get(user);
        if (followers != null) {
            for (String neighbor : followers) {
                if (!visited.contains(neighbor)) {
                    dfsReverse(neighbor, visited, scc);
                }
            }
        }
    }

    // Helper method for findCliques()
    private void dfsGraph(String user, Set<String> visited, Deque<String> finished) {
        visited.add(user);
        List<String> following = graph.get(user);
        if (following != null) {
            for (String follows : following) {
                if (!visited.contains(follows)) {
                    dfsGraph(follows, visited, finished);
                }
            }
        }
        //Adding user inorder of decreasing finishing times.
        finished.push(user);
    }

	/**
     * Calculates the "broadcast reach" of a user's post.
     * This includes all users who follow the given user, directly or indirectly.
     * @param userId The ID of the user whose post reach is being calculated.
     * @return A set of user IDs representing the total audience.
     */
    
    //Just running DFS to find the limit of all traversable nodes from user.
	public Set<String> getBroadcastReach(String userId) {
	    Set<String> reach = new HashSet<>();
	    dfsReach(userId, reach);
	    return reach;
	}
	
	//Helper method for getBroadcastReach()
	private void dfsReach(String userId, Set<String> reach) {
	    if (!reach.add(userId)) return; // already visited

	    List<String> followers = reverseGraph.get(userId);
	    if (followers != null) {
	        for (String follower : followers) {
	        	dfsReach(follower, reach);
	        }
	    }
	}


    
    
    /**
     * Finds all users whose follower count is within a specified range.
     * @param minFollowers The minimum follower count (inclusive).
     * @param maxFollowers The maximum follower count (inclusive).
     * @return A sorted list of user IDs that fall within the influence range.
     */
	//1. Builds BST of user and followers 2. filters user using the min and max 3. Returns results
    public List<String> getUsersInInfluenceRange(int minFollowers, int maxFollowers) {
        BSTNode root = buildBST(influencerScores); // build BST from your map
        List<String> result = new ArrayList<>();
        queryRange(root, minFollowers, maxFollowers, result);
        return result;
    }
    
    // Recursively traversing followers using a BST to find their following, and only adding followers with a following
    // that is in range.
    private void queryRange(BSTNode node, int min, int max, List<String> result) {
        if (node == null) return;

        if (node.score > min) {
            queryRange(node.left, min, max, result);
        }

        if (node.score >= min && node.score <= max) {
            result.addAll(node.users);
        }

        if (node.score < max) {
            queryRange(node.right, min, max, result);
        }
    }
    
    
    //BST Helper class
    private static class BSTNode {
        int score;
        List<String> users = new ArrayList<>();
        BSTNode left, right;
        
        BSTNode(int score) { this.score = score; }
    }
    
    //BST generator
    private BSTNode buildBST(Map<String, Integer> influenceMap) {
        BSTNode root = null;
        for (Map.Entry<String, Integer> entry : influenceMap.entrySet()) {
            root = insert(root, entry.getValue(), entry.getKey());
        }
        return root;
    }
    
    //Helper to buildBST
    private BSTNode insert(BSTNode root, int influence, String userId) {
        if (root == null) {
            BSTNode node = new BSTNode(influence);
            node.users.add(userId);
            return node;
        }

        if (influence < root.score) {
            root.left = insert(root.left, influence, userId);
        } else if (influence > root.score) {
            root.right = insert(root.right, influence, userId);
        } else {
            root.users.add(userId); // same influence, just append
        }
        return root;
    }

}
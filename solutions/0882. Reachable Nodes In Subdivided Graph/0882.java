class Solution {
  public int reachableNodes(int[][] edges, int maxMoves, int n) {
    List<Pair<Integer, Integer>>[] graph = new List[n];
    Queue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]); // (d, u)
    int[] dist = new int[n];
    Arrays.fill(dist, maxMoves + 1);

    for (int i = 0; i < n; ++i)
      graph[i] = new ArrayList<>();

    for (int[] edge : edges) {
      final int u = edge[0];
      final int v = edge[1];
      final int cnt = edge[2];
      graph[u].add(new Pair<>(v, cnt));
      graph[v].add(new Pair<>(u, cnt));
    }

    minHeap.offer(new int[] {0, 0});
    dist[0] = 0;

    while (!minHeap.isEmpty()) {
      final int d = minHeap.peek()[0];
      final int u = minHeap.poll()[1];
      // Already takes `maxMoves` to reach `u`, can't explore anymore.
      if (d >= maxMoves)
        break;
      for (Pair<Integer, Integer> pair : graph[u]) {
        final int v = pair.getKey();
        final int w = pair.getValue();
        final int newDist = d + w + 1;
        if (newDist < dist[v]) {
          dist[v] = newDist;
          minHeap.offer(new int[] {newDist, v});
        }
      }
    }

    final int reachableNodes = (int) Arrays.stream(dist).filter(d -> d <= maxMoves).count();
    int reachableSubnodes = 0;

    for (int[] edge : edges) {
      final int u = edge[0];
      final int v = edge[1];
      final int cnt = edge[2];
      // Reachable nodes of `edge` from `u`.
      final int a = dist[u] > maxMoves ? 0 : Math.min(maxMoves - dist[u], cnt);
      // Reachable nodes of `edge` from `v`.
      final int b = dist[v] > maxMoves ? 0 : Math.min(maxMoves - dist[v], cnt);
      reachableSubnodes += Math.min(a + b, cnt);
    }

    return reachableNodes + reachableSubnodes;
  }
}

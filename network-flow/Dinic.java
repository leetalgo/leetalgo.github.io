import java.util.*;

class Dinic {
    long dinic(V s, V t) {
        long flow = 0;
        for (int p = 1; ; p++) {
            Queue<V> que = new LinkedList<>();
            s.level = 0;
            s.p = p;
            que.offer(s);
            while (!que.isEmpty()) {
                V v = que.poll();
                v.iter = v.es.size() - 1;
                for (E e : v.es) {
                    if (e.to.p != p && e.cap > 0) {
                        e.to.level = v.level + 1;
                        e.to.p = p;
                        que.offer(e.to);
                    }
                }
            }
            if (t.p != p) return flow;
            for (long f; (f = dfs(s, t, Integer.MAX_VALUE)) > 0; ) flow += f;
        }
    }

    long dfs(V v, V t, long f) {
        if (v == t) return f;
        for (; v.iter >= 0; v.iter--) {
            E e = v.es.get(v.iter);
            if (v.level < e.to.level && e.cap > 0) {
                long d = dfs(e.to, t, Math.min(f, e.cap));
                if (d > 0) {
                    e.cap -= d;
                    e.rev.cap += d;
                    return d;
                }
            }
        }
        return 0;
    }

    class V {
        ArrayList<E> es = new ArrayList<>();
        int level;
        int p;
        int iter;

        void add(V to, long cap) {
            E e = new E(to, cap), rev = new E(this, 0);
            e.rev = rev; rev.rev = e;
            es.add(e); to.es.add(rev);
        }
    }

    class E {
        V to;
        E rev;
        long cap;

        E(V to, long cap) {
            this.to = to;
            this.cap = cap;
        }
    }

    public static void main(String[] args) {
        new Dinic().example();
    }

    void example() {
        V s = new V();
        V t = new V();
        V a = new V();
        V b = new V();
        s.add(a, 1);
        s.add(b, 2);
        b.add(a, 1);
        a.add(t, 2);
        b.add(t, 1);
        long flow = dinic(s, t);
        System.out.println("Flow should be 3");
        System.out.printf("Flow executed = %d%n", flow);
    }
}

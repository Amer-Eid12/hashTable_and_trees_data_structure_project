import java.util.List;

public class AVLTree {
    Node root;

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    private int getBalance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    public Node insert(Node node, Movie movie) {
        if (node == null) {
            return new Node(movie);
        }

        if (movie.getTitle().compareTo(node.movie.getTitle()) < 0) {
            node.left = insert(node.left, movie);
        } else if (movie.getTitle().compareTo(node.movie.getTitle()) > 0) {
            node.right = insert(node.right, movie);
        } else {
            return node; 
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && movie.getTitle().compareTo(node.left.movie.getTitle()) < 0) {
            return rotateRight(node);
        }

        if (balance < -1 && movie.getTitle().compareTo(node.right.movie.getTitle()) > 0) {
            return rotateLeft(node);
        }

        if (balance > 1 && movie.getTitle().compareTo(node.left.movie.getTitle()) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && movie.getTitle().compareTo(node.right.movie.getTitle()) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    public Node delete(Node node, String title) {
        if (node == null) {
            return node;
        }

        if (title.compareTo(node.movie.getTitle()) < 0) {
            node.left = delete(node.left, title);
        } else if (title.compareTo(node.movie.getTitle()) > 0) {
            node.right = delete(node.right, title);
        } else {
            if ((node.left == null) || (node.right == null)) {
                Node temp = (node.left != null) ? node.left : node.right;

                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                Node temp = getMinValueNode(node.right);
                node.movie = temp.movie;
                node.right = delete(node.right, temp.movie.getTitle());
            }
        }

        if (node == null) {
            return node;
        }

        node.height = Math.max(height(node.left), height(node.right)) + 1;

        int balance = getBalance(node);

        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }

        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }

        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node getMinValueNode(Node node) {
        Node current = node;

        while (current.left != null) {
            current = current.left;
        }

        return current;
    }

    public void inorderTraversal(Node root, Movie[] movies, int index) {
        if (root == null) {
            return;
        }
        inorderTraversal(root.left, movies, index);
        movies[index++] = root.movie;
        inorderTraversal(root.right, movies, index);
    }

    public void inorderTraversal(Node node, List<Movie> result) {
        if (node != null) {
            inorderTraversal(node.left, result);
            result.add(node.movie);
            inorderTraversal(node.right, result);
        }
    }

    public int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    public int getSize(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + getSize(node.left) + getSize(node.right);
    }
}

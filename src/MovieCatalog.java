import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MovieCatalog {
    AVLTree[] table;
    int size;

    public MovieCatalog(int initialSize) {
        this.size = initialSize;
        this.table = new AVLTree[initialSize];
        for (int i = 0; i < initialSize; i++) {
            table[i] = new AVLTree();
        }
    }

    private long hash(String str, int ts){
        int i=0;
        long hashValue = 0;
        while(i!=str.length()){
            hashValue = (hashValue<<5)+str.charAt(i++);
        }
        return hashValue%ts;
    }

    public void allocate(int newSize) {
        AVLTree[] newTable = new AVLTree[newSize];
        for (int i = 0; i < newSize; i++) {
            newTable[i] = new AVLTree();
        }

        for (AVLTree tree : table) {
            if (tree.root != null) {
                Movie[] movies = new Movie[tree.getSize(tree.root)];
                tree.inorderTraversal(tree.root, movies, 0);

                for (Movie movie : movies) {
                    if (movie == null) {
                        continue;
                    }
                    long newIndex = hash(movie.getTitle(), newSize);
                    newTable[(int) newIndex].root = newTable[(int) newIndex].insert(newTable[(int) newIndex].root, movie);
                }
            }
        }

        this.table = newTable;
        this.size = newSize;
    }

    public void put(Movie movie) {
        int index = (int) hash(movie.getTitle(), size);
        table[index].root = table[index].insert(table[index].root, movie);

        if (getAverageHeight()>3) {
            allocate(size * 2);
        }
    }

    public Movie get(String title) {
        int index = (int) hash(title, size);
        Node node = search(table[index].root, title);
        return node == null ? null : node.movie;
    }

    public void erase(String title) {
        int index = (int) hash(title, size);
        table[index].root = table[index].delete(table[index].root, title);
    }

    private Node search(Node node, String title) {
        if (node == null || title.equals(node.movie.getTitle())) {
            return node;
        }

        if (title.compareTo(node.movie.getTitle()) < 0) {
            return search(node.left, title);
        } else {
            return search(node.right, title);
        }
    }

    public void deallocate() {
        for (int i = 0; i < size; i++) {
            table[i] = null;
        }
        table = null;
        size = 0;
    }

    public double getAverageHeight() {
        int totalHeight = 0;
        int nonEmptyTrees = 0;

        for (AVLTree tree : table) {
            if (tree.root != null) {
                totalHeight += tree.getHeight(tree.root);
                nonEmptyTrees++;
            }
        }

        return nonEmptyTrees == 0 ? 0 : (double) totalHeight / nonEmptyTrees;
    }

    public void saveMoviesToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (AVLTree tree : table) {
                if (tree.root == null) {
                    continue;
                }

                int treeSize = tree.getSize(tree.root);
                Movie[] movies = new Movie[treeSize];
                tree.inorderTraversal(tree.root, movies, 0);

                for (Movie movie : movies) {
                    if (movie != null) {
                        writer.write("Title: " + movie.getTitle() + "\n");
                        writer.write("Description: " + movie.getDescription() + "\n");
                        writer.write("Release Year: " + movie.getReleaseYear() + "\n");
                        writer.write("Rating: " + movie.getRating() + "\n\n");
                    }
                }
            }
        }
    }

    public void loadMoviesFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Title: ")) {
                    String title = line.substring(7);
                    String description = reader.readLine().substring(13);
                    int releaseYear = Integer.parseInt(reader.readLine().substring(14));
                    double rating = Double.parseDouble(reader.readLine().substring(8));
                    reader.readLine();

                    Movie movie = new Movie(title, description, releaseYear, rating);
                    put(movie);
                }
            }
        }
    }

    public ObservableList<Movie> getAllMovies() {
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        for (AVLTree tree : table) {
            if (tree.root != null) {
                tree.inorderTraversal(tree.root, movies);
            }
        }
        return movies;
    }

    public ObservableList<Movie> searchMovies(String title) {
        ObservableList<Movie> allMovies = getAllMovies();
        ObservableList<Movie> result = FXCollections.observableArrayList();

        if (title == null || title.trim().isEmpty()) {
            return allMovies;
        }

        for (Movie movie : allMovies) {
            if (movie.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(movie);
            }
        }
        return result;
    }

    public Movie getTopRatedMovie() {
        Movie topRated = null;
        for (AVLTree tree : table) {
            if (tree.root != null) {
                List<Movie> movies = new ArrayList<>();
                tree.inorderTraversal(tree.root, movies);
                for (Movie movie : movies) {
                    if (topRated == null || movie.getRating() > topRated.getRating()) {
                        topRated = movie;
                    }
                }
            }
        }
        return topRated;
    }

    public Movie getLeastRatedMovie() {
        Movie leastRated = null;
        for (AVLTree tree : table) {
            if (tree.root != null) {
                List<Movie> movies = new ArrayList<>();
                tree.inorderTraversal(tree.root, movies);
                for (Movie movie : movies) {
                    if (leastRated == null || movie.getRating() < leastRated.getRating()) {
                        leastRated = movie;
                    }
                }
            }
        }
        return leastRated;
    }

    public ObservableList<Movie> sortMoviesByTitleAsc() {
        ObservableList<Movie> movies = getAllMovies();
        FXCollections.sort(movies, (m1, m2) -> m1.getTitle().compareToIgnoreCase(m2.getTitle()));
        return movies;
    }

    public ObservableList<Movie> sortMoviesByTitleDesc() {
        ObservableList<Movie> movies = getAllMovies();
        FXCollections.sort(movies, (m1, m2) -> m2.getTitle().compareToIgnoreCase(m1.getTitle()));
        return movies;
    }
}



import com.google.common.base.Joiner;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.jgrapht.GraphPath;
import org.jgrapht.ListenableGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BuildThemeGraph {

    private static class ResultList {

        private final ArrayList<Result> results = new ArrayList<>(50);
        private Result max = null;

        private class Result {
            private Double distance;
            private String id;
            private List<Vertex> path;

            private Result(Double distance, String id, List<Vertex> path) {
                this.distance = distance;
                this.id = id;
                this.path = path;
            }

            @Override
            public String toString() {
                Double maxDistance = max.distance;
                return "Recommended photo " + id + " with probability " + distance / maxDistance +
                        " because you like " +
                        String.join(" then you like ",
                                path.stream().map(Vertex::getValue).collect(Collectors.joining())
                        );
            }
        }

        public void merge(GraphPath<Vertex, DefaultWeightedEdge> result) {
            if (results.size() < 50) {
                Result resultEntity = new Result(result.getWeight(),
                        result.getEndVertex().getValue(),
                        result.getVertexList());
                results.add(resultEntity);
            } else if (max.distance > result.getWeight()) {
                int indexOfMax = results.indexOf(max);
                Result resultEntity = new Result(result.getWeight(),
                        result.getEndVertex().getValue(),
                        result.getVertexList());
                results.set(indexOfMax, resultEntity);
            } else {
                return;
            }
            max = Collections.max(results, Comparator.comparing(o -> o.distance));
        }

        @Override
        public String toString() {
            Collections.sort(results, (o1, o2) -> o2.distance.compareTo(o1.distance));
            return results.toString();
        }
    }

    static class UserInfo {
        private String value;
        private Double probability;

        UserInfo(String value, Double probability) {
            this.value = value;
            this.probability = probability;
        }

        public Double getProbability() {
            return probability;
        }

        public void setProbability(Double probability) {
            this.probability = probability;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static void main(String args[]) throws IOException {
        //File gModel = new File("D:\\wordvec.bin");
        WordVectors wordVectors = WordVectorSerializer.readWord2VecModel("D:\\wordvec.bin", true);
        ListenableGraph<Vertex, DefaultWeightedEdge> graph =
                new DefaultListenableGraph<>(new WeightedMultigraph<>(DefaultWeightedEdge.class));

        File file = new File(Const.TAGS_PATH);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new HSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet("Лист1");
        List<Vertex> photoVerticles = new LinkedList<>();
        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            System.out.println(i + "/" + sheet.getLastRowNum());
            Row row = sheet.getRow(i);
            String photoID = row.getCell(0).getStringCellValue();
            float probability = (float) row.getCell(1).getNumericCellValue();
            String themeValue = row.getCell(2).getStringCellValue();
            Vertex photo = new Vertex(photoID, Vertex.Type.PHOTO);
            if (!graph.containsVertex(photo)) {
                graph.addVertex(photo);
                photoVerticles.add(photo);
            }
            Vertex theme = new Vertex(themeValue.toLowerCase(), Vertex.Type.THEME);
            if (!graph.containsVertex(theme)) {
                graph.addVertex(theme);
                Collection<String> synonims = wordVectors.wordsNearest(themeValue, 15);
                for (String sinonym : synonims) {
                    String value = sinonym.toLowerCase();
                    Vertex sinonymTheme = new Vertex(value, Vertex.Type.THEME);
                    if (!graph.containsVertex(sinonymTheme)) {
                        graph.addVertex(sinonymTheme);
                        DefaultWeightedEdge edge = new DefaultWeightedEdge();
                        graph.addEdge(theme, sinonymTheme, edge);
                        double similarity = wordVectors.similarity(themeValue, sinonym);
                        graph.setEdgeWeight(edge, 1 - similarity);
                    }
                }
            }

            DefaultWeightedEdge edge = new DefaultWeightedEdge();
            graph.addEdge(photo, theme, edge);
            graph.setEdgeWeight(edge, (1 - probability) * 2);
        }

        inputStream.close();

        List<UserInfo> userInfos = new LinkedList<>();
        userInfos.add(new UserInfo("football", 0.64));
        userInfos.add(new UserInfo("grass", 0.54));

        Vertex user = new Vertex("Ivan", Vertex.Type.USER);
        graph.addVertex(user);
        for (UserInfo userInfo : userInfos) {
            Vertex theme = new Vertex(userInfo.value, Vertex.Type.THEME);
            DefaultWeightedEdge edge = new DefaultWeightedEdge();
            graph.addEdge(user, theme, edge);
            graph.setEdgeWeight(edge, userInfo.probability);
        }
        ResultList recommended = new ResultList();
        System.out.println("user vertex ");
        DijkstraShortestPath<Vertex, DefaultWeightedEdge> algorithm = new DijkstraShortestPath<>(graph, 7);
        ShortestPathAlgorithm.SingleSourcePaths<Vertex, DefaultWeightedEdge> paths = algorithm.getPaths(user);
        for (Vertex photoVertex : photoVerticles) {
            GraphPath<Vertex, DefaultWeightedEdge> path = paths.getPath(photoVertex);
            recommended.merge(path);
        }
        System.out.println(recommended);
/*
        JGraphXAdapter<String, DefaultWeightedEdge> jGraphXAdapter = new JGraphXAdapter(graph);


        JFrame jFrame = new JFrame("Interest graph");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mxGraphLayout layout = new mxCircleLayout(jGraphXAdapter);
        layout.execute(jGraphXAdapter.getDefaultParent());
        jFrame.add(new mxGraphComponent(jGraphXAdapter));
        jFrame.pack();
        jFrame.setLocationByPlatform(true);
        jFrame.setVisible(true);*/
    }

}

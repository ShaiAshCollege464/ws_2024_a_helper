import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
//    public static void main(String[] args) {
//        List<List<String>> lines = readFile();
//        System.out.println();
//        //Write your code here!
//    }


    public static List<List<String>> readFile () {
        List<List<String>> lines = new ArrayList<>();
        try {
            File file = new File("data.csv");
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] tokens = line.split(",");
                    List<String> tokensList = new ArrayList<>(Arrays.asList(tokens));
                    lines.add(tokensList);
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }


    private static final int IMAGE_WIDTH = 800;
    private static final int IMAGE_HEIGHT = 600;
    private static final int MIN_SQUARES = 200;
    private static final int MAX_SQUARES = 300;
    private static final Color[] COLOR_BANK = createColorBank(50);

    public static void main(String[] args) {
        IntStream.range(1,100).forEach(number -> {
            calculateThirdMostDominantColor(number, String.format("images/%d.png", number));

        });
    }

    public static void calculateThirdMostDominantColor(int index, String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            Map<Integer, Integer> colorFrequencyMap = new HashMap<>();

            // Count color frequencies
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int rgb = image.getRGB(x, y);
                    // Skip white color
                    if (rgb != 0xFFFFFFFF) {
                        colorFrequencyMap.put(rgb, colorFrequencyMap.getOrDefault(rgb, 0) + 1);
                    }
                }
            }

            // Sort colors by frequency
            List<Map.Entry<Integer, Integer>> sortedColors = colorFrequencyMap.entrySet()
                    .stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .collect(Collectors.toList());

            // Get the third most dominant color
            if (sortedColors.size() < 3) {
                System.out.println("Not enough colors to determine the third most dominant one.");
                return;
            }

            int thirdMostDominantColor = sortedColors.get(2).getKey();
            int red = (thirdMostDominantColor >> 16) & 0xFF;
            int green = (thirdMostDominantColor >> 8) & 0xFF;
            int blue = thirdMostDominantColor & 0xFF;

            System.out.println(String.format("%d,%d,%d,%d", index, red, green, blue));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage createRandomSquaresImage() {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        Random random = new Random();

        int numberOfSquares = MIN_SQUARES + random.nextInt(MAX_SQUARES - MIN_SQUARES + 1);

        for (int i = 0; i < numberOfSquares; i++) {
            int x = random.nextInt(IMAGE_WIDTH);
            int y = random.nextInt(IMAGE_HEIGHT);
            int size = 10 + random.nextInt(91); // size between 10 and 100
            Color color = COLOR_BANK[random.nextInt(COLOR_BANK.length)];
            g.setColor(color);
            g.fillRect(x, y, size, size);
        }

        g.dispose();
        return image;
    }

    private static Color[] createColorBank(int numberOfColors) {
        Color[] colorBank = new Color[numberOfColors];
        Random random = new Random();
        for (int i = 0; i < numberOfColors; i++) {
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            colorBank[i] = new Color(r, g, b);
        }
        return colorBank;
    }

    private static void saveImage(BufferedImage image, String filename) {
        try {
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

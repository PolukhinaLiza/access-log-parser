import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());

        // Задание по теме "Циклы"
        int fileNumber = 1;
        while (true){
            System.out.println("Введите путь к файлу");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (!fileExists) {
                System.out.println("Вы ввели путь к несуществующему файлу");
                continue;
            } else if (isDirectory){
                System.out.println("Вы ввели путь к папке");
                continue;
            }
            System.out.println("Путь указан верно");
            System.out.println("Это файл номер " + fileNumber++ );
        }

    }
}

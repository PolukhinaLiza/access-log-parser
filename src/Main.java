import java.io.*;
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

            //Задание по обработке исключений
            int totalLines = 0;
            int minLength = Integer.MAX_VALUE;
            int maxLength = 0;
            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader =
                        new BufferedReader(fileReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    totalLines++;
                    int length = line.length();

                    if (length > 1024) {
                        throw new LineTooLongException("Строка превышает 1024 символа, номер строки: " + totalLines);
                    }

                    if (length < minLength) minLength = length;
                    if (length > maxLength) maxLength = length;
                }
                System.out.println("Всего строк в файле: " + totalLines);
                System.out.println("Длина самой длинной строки в файле: " + maxLength);
                System.out.println("Длина самой короткой строки в файле: " + (minLength == Integer.MAX_VALUE ? 0 : minLength));
            } catch (LineTooLongException ex) {
                System.out.println(ex.getMessage());
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}

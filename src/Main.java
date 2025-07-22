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

            //Задание по обработке исключений и сравнении объектов
            int totalLines = 0;
            int totalRequests = 0;
            int requestsYandexBot = 0;
            int requestsGoogleBot = 0;
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

                    String userAgent = extractUserAgent(line);
                    if (userAgent==null) continue;
                    totalRequests++;
                    String fragment = extractFragmentUserAgent(userAgent);
                    if (fragment==null) continue;
                    String botName = getBotName(fragment);
                    if (botName== null) continue;

                    if (botName.equals("YandexBot")) {
                        requestsYandexBot++;
                    }
                    if (botName.equals("Googlebot")) {
                        requestsGoogleBot++;
                    }


                }
                double yandexBot = (double)requestsYandexBot/totalRequests * 100;
                double googleBot = (double)requestsGoogleBot/totalRequests * 100;


                System.out.println("Всего строк в файле: " + totalLines);
                System.out.println("Доля запросов от YandexBot: " + yandexBot);
                System.out.println("Доля запросов от Googlebot " + googleBot);
                System.out.println(requestsGoogleBot);
                System.out.println(requestsYandexBot);
                System.out.println(totalRequests);
            } catch (LineTooLongException ex) {
                System.out.println(ex.getMessage());
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    //метод для поиска части user-agent
    private static String extractUserAgent(String line) {
        int lastQuoteIndex = line.lastIndexOf('"');
        if (lastQuoteIndex != -1) {
            int secondLastQuoteIndex = line.lastIndexOf('"', lastQuoteIndex - 1);
            if (secondLastQuoteIndex != -1) {
                return line.substring(secondLastQuoteIndex + 1, lastQuoteIndex).trim();
            }
        }
        return null;
    }

    //метод для поиска фрагмента
    private static String extractFragmentUserAgent(String userAgent) {
        int firstOpenBracketIndex = userAgent.indexOf('(');
        int firstCloseBracketIndex = userAgent.indexOf(')', firstOpenBracketIndex);

        if (firstOpenBracketIndex != -1 && firstCloseBracketIndex != -1) {
            String firstBrackets = userAgent.substring(firstOpenBracketIndex + 1, firstCloseBracketIndex);
            String[] parts = firstBrackets.split(";");
            if (parts.length >= 2) {
                return  parts[1].trim(); // второй элемент после разделения без пробелов в начале и конце
            }
        }
        return null; //
    }

    //метод для поиска названия
    private static String getBotName(String fragment) {
        int index = fragment.indexOf('/');

        if (index != -1) {
            return fragment.substring(0, index);
        }
        return null; //
    }
}

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Saves / loads tasks to a text file. One line = title|description|subject|yyyy-MM-dd|status */
public class StorageService {

    private final String fileName;

    public StorageService(String fileName) {
        this.fileName = fileName;
    }

    // '|' and line breaks would break the file format, so replace them with spaces
    private static String clean(String s) {
        return s == null ? "" : s.replace("|", " ").replace("\r", " ").replace("\n", " ");
    }

    public void saveTasks(List<Task> tasks) {
        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (Task t : tasks) {
                writer.write(clean(t.getTitle()) + "|"
                        + clean(t.getDescription()) + "|"
                        + clean(t.getSubject().getName()) + "|"
                        + t.getDeadline() + "|"
                        + t.getStatus() + "\n");
            }
        } catch (IOException e) {
            System.out.println("บันทึกข้อมูลไม่สำเร็จ: " + e.getMessage());
        }
    }

    public ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return tasks;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length < 5) continue;                 // skip broken lines
                try {
                    Task task = new Task(p[0], p[1], new Subject(p[2]), LocalDate.parse(p[3]));
                    task.setStatus(p[4]);
                    tasks.add(task);
                } catch (Exception e) {
                    System.out.println("ข้ามบรรทัดที่อ่านไม่ได้: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("โหลดข้อมูลไม่สำเร็จ: " + e.getMessage());
        }
        return tasks;
    }

    public void clearAll() {
        new File(fileName).delete();
    }
}

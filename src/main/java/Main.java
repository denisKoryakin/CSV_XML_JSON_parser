import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> csvList = parseCSV(columnMapping, fileName);
        List<Employee> xmlList = parseXML("./data.xml");
        String json = listToJson(csvList);
        String json1 = listToJson(xmlList);
        writeString(json1);
        for (int i = 0; i < csvList.size(); i++) {
            System.out.println(jsonToList("data.json").get(i));
        }
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return staff;
    }

    public static List<Employee> parseXML(String pathName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new File(pathName));
        Node root = doc.getDocumentElement();
        return read(root);
    }

    public static List<Employee> read(Node node) {
        List<Employee> employees = new ArrayList<>();
        List<String> validValues = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            NodeList nodeList1 = nodeList.item(i).getChildNodes();
            for (int j = 0; j < nodeList1.getLength(); j++) {
                if ((nodeList1.item(j).getNodeName()).equals("id")) {
                    validValues.add(nodeList1.item(j).getTextContent());
                }
                if ((nodeList1.item(j).getNodeName()).equals("firstName")) {
                    validValues.add(nodeList1.item(j).getTextContent());
                }
                if ((nodeList1.item(j).getNodeName()).equals("lastName")) {
                    validValues.add(nodeList1.item(j).getTextContent());
                }
                if ((nodeList1.item(j).getNodeName()).equals("country")) {
                    validValues.add(nodeList1.item(j).getTextContent());
                }
                if ((nodeList1.item(j).getNodeName()).equals("age")) {
                    validValues.add(nodeList1.item(j).getTextContent());
                }
            }
        }
        for (int i = 0; i < validValues.size(); i += 5) {
            Employee employee = new Employee(Long.parseLong(validValues.get(i)), validValues.get(i + 1), validValues.get(i + 2), validValues.get(i + 3), Integer.parseInt(validValues.get(i + 4)));
            employees.add(employee);
        }
        return employees;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    public static void writeString(String json) {
        try (FileWriter file = new FileWriter("data.json", false)) {
            file.write(json);
            file.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static List<Employee> jsonToList(String json) {
        List<Employee> staff = new ArrayList<>();
        JsonParser parser = new JsonParser();
        try {
            Object obj = parser.parse(new FileReader(json));
            JsonArray array = (JsonArray) obj;
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            for (int i = 0; i < array.size(); i++) {
                Employee employee = gson.fromJson(String.valueOf(array.get(i)), Employee.class);
                staff.add(employee);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return staff;
    }
}




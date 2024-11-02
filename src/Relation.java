import java.util.*;

public class Relation {

    /** The name of the relation table. */
    public String name;

    /** The name of all the attributes. */
    public ArrayList<String> attributes = new ArrayList<>();

    /** The name of the attributes that are non-atomic. */
    public ArrayList<String> nonAtomicAttributes = new ArrayList<String>();

    /** The tuples contained in the relation. */
    public ArrayList<ArrayList<String>> data = new ArrayList<>();

    /** The relation's primary key. */
    public ArrayList<String> primaryKey = new ArrayList<>();

    /** User input object. */
    static private final Scanner scanner = new Scanner(System.in);

    /** Creates a relation table through user input (command line). */
    public Relation() {
        System.out.println();
        getName();
        getAttributes();
        getNonAtomicAttributes();
        getPrimaryKey();
        getData();
        print();
        Normalizer.normalize1NF(this);
    }

    /**
     * Creates a relation table based on a list of column data.
     * @param name the name of the relation.
     * @param columns a list of columns and their data. (must be in the same order as the attributes list)
     * @param attributes the attributes that define the columns. (must be in the same order as the columns list)
     * @param primaryKey the relations primary key.
     * @param nonAtomicAttributes the name of the attributes that are non-atomic.
     */
    public Relation(String name, ArrayList<ArrayList<String>> columns, ArrayList<String> attributes, ArrayList<String> primaryKey, ArrayList<String> nonAtomicAttributes) {
        this.name = name;
        this.attributes = attributes;
        this.primaryKey = primaryKey;
        this.nonAtomicAttributes = nonAtomicAttributes;
        data = Relation.stitchColumns(columns);
    }

    /** Gets the name of the relation table from user input. */
    private void getName() {
        System.out.println("NAME");
        System.out.print("Enter the relation's name: ");
        name = scanner.nextLine();
        System.out.println();
    }

    /** Gets the relation's attributes from user input. */
    private void getAttributes() {
        System.out.println("ATTRIBUTES");
        System.out.print("Enter the total number of attributes: ");
        int attributeCount = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character left in buffer by nextInt()

        // Add the attribute names to attributes list
        for(int i=1; i<=attributeCount; i++) {
            System.out.printf("Enter the name of attribute #%d: ", i);
            attributes.add(scanner.nextLine());
        }
        System.out.println();
    }

    /** Gets the relation's non-atomic attributes from user input. */
    private void getNonAtomicAttributes() {
        System.out.println("NON-ATOMIC ATTRIBUTES");
        System.out.print("Enter the number of attributes that are non atomic: ");
        int attributeCount = scanner.nextInt();
        scanner.nextLine();

        String attribute;

        for(int i=1; i<=attributeCount; i++) {
            System.out.printf("Enter the name of attribute #%d: ", i);
            attribute = scanner.nextLine();

            if(attributes.contains(attribute)) {
                nonAtomicAttributes.add(attribute);
            } else {
                System.out.println("ERROR: Attribute does not exist");
                i--;
            }
        }
        System.out.println();
    }

    /** Gets the relation's primary key from user input. */
    private void getPrimaryKey() {
        System.out.println("PRIMARY KEY");
        System.out.print("Enter the number of attributes that make up the primary key: ");
        int attributeCount = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character left in buffer by nextInt()

        String attribute;

        for(int i=1; i<=attributeCount; i++) {
            System.out.printf("Enter the name of attribute #%d: ", i);
            attribute = scanner.nextLine();

            if(attributes.contains(attribute)) {
                primaryKey.add(attribute);
            } else {
                System.out.println("ERROR: Attribute does not exist");
                i--;
            }
        }
        System.out.println();
    }

    /** Gets the relation's tuples from user input. */
    private void getData() {
        System.out.println("DATA ENTRY");
        System.out.print("Enter the total number of tuples: ");
        int tupleCount = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character left in buffer by nextInt()

        for (int i = 0; i < tupleCount; i++) {
            ArrayList<String> tempTuple = new ArrayList<>(attributes.size());
            System.out.printf("%nTUPLE #%d:%n", i + 1);
            for (int j = 0; j < attributes.size(); j++) {
                System.out.printf("Enter the value for %s: ", attributes.get(j));
                tempTuple.add(j, scanner.nextLine());
            }
            data.add(tempTuple);
        }
        System.out.println();
    }

    /** Prints the relation table. */
    public void print() {
        System.out.println();
        // Print attribute names
        System.out.println("------" + name + "-------");
        for(String attribute : attributes) {
            System.out.printf("%-25s", attribute);
        }

        System.out.println();

        for(ArrayList<String> tuple : data) {
            for(String value : tuple) {
                System.out.printf("%-25s", value);
            }
            System.out.println();
        }
    }

    /**
     * Gets all the data in the given column of the given relation.
     * @param relation the relation table that contains the column.
     * @param attribute the attribute associated with the column.
     * @return the data in the column.
     */
    static public ArrayList<String> getColumn(Relation relation, String attribute) {
        ArrayList<String> columnValues = new ArrayList<>();
        int attributeColumn = relation.attributes.indexOf(attribute);

        for(ArrayList<String> tuple : relation.data) {
            columnValues.add(tuple.get(attributeColumn));
        }

        return columnValues;
    }

    /**
     * Removes a column from a relation.
     * @param relation the relation that contains the column.
     * @param attribute the attribute associated with the column.
     * @return the relation without the specified column.
     */
    static public Relation removeColumn(Relation relation, String attribute) {

        int attributeIndex = relation.attributes.indexOf(attribute);

        for(ArrayList<String> tuple : relation.data) {
            tuple.remove(attributeIndex);
        }

        relation.attributes.remove(attributeIndex);

        return relation;
    }

    /**
     * Combines a list of column data into a single table.
     * @param columns a list of the columns and their data.
     * @return the columns transformed into a proper relation table.
     */
    static private ArrayList<ArrayList<String>> stitchColumns(ArrayList<ArrayList<String>> columns) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        ArrayList<String> tuple = new ArrayList<>();

        for(int i=0; i<columns.get(0).size(); i++) {
            for(ArrayList<String> column : columns) {
                tuple.add(column.get(i));
            }
            result.add((ArrayList<String>) tuple.clone());
            tuple.clear();
        }

        return result;
    }


}

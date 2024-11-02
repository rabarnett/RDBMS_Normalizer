import java.util.ArrayList;
import java.util.Arrays;

public class Normalizer {


    // -------1NF-------------

    /**
     * Performs the first normal form on a relation
     * @param relation the relation that will be normalized.
     */
    static public ArrayList<Relation> normalize1NF(Relation relation) {

        ArrayList<Relation> relations1NF = new ArrayList<>();
        ArrayList<ArrayList<String>> columns = new ArrayList<>();
        ArrayList<String> attributes = (ArrayList<String>) relation.primaryKey.clone();

        // Adds the prime attribute columns to the new relations.
        for(String primeAttribute: relation.primaryKey) {
            columns.add(Relation.getColumn(relation, primeAttribute));
        }

        for(String nonAtomicAttribute : relation.nonAtomicAttributes) {

            // Defines & constructs relational table relevant to current non-atomic attribute.
            columns.add(Relation.getColumn(relation, nonAtomicAttribute));
            attributes.add(nonAtomicAttribute);
            relation.primaryKey.add(nonAtomicAttribute);
            ArrayList<String> nonAtomicAttributes = new ArrayList<>(Arrays.asList(nonAtomicAttribute));
            Relation newRelation = new Relation(
                    relation.name + "_" + nonAtomicAttribute,
                    (ArrayList<ArrayList<String>>) columns.clone(),
                    (ArrayList<String>) attributes.clone(),
                    (ArrayList<String>) relation.primaryKey.clone(),
                    (ArrayList<String>) nonAtomicAttributes.clone()
            );

            // Break non atomic attribute into multiple tuples
            parseNonAtomicAttributes(newRelation);
            relations1NF.add(newRelation);
            columns.removeLast();
            attributes.removeLast();
        }

        // Updates the original relation table
        removeNonAtomicAttributes(relation);
        relations1NF.add(relation);

        // Prints the results
        System.out.println("\nNORMALIZE 1NF");
        for(Relation r : relations1NF)
            r.print();

        return relations1NF;
    }

    /**
     * Removes non-atomic attributes from a relation.
     * @param relation the relation containing non-atomic attributes.
     */
    static private void removeNonAtomicAttributes(Relation relation) {

        for(String nonAtomicAttribute : relation.nonAtomicAttributes) {
            Relation.removeColumn(relation, nonAtomicAttribute);
        }

        relation.nonAtomicAttributes.clear();
    }

    /**
     * Creates new tuples for each atomic data in the relations non-atomic attributes
     * @param relation the relation containing non-atomic attributes.
     */
    static private void parseNonAtomicAttributes(Relation relation) {
        int attributeIndex;
        ArrayList<ArrayList<String>> newTuples = new ArrayList<>();

        for(String attribute : relation.nonAtomicAttributes) {
            attributeIndex = relation.attributes.indexOf(attribute);
            for(int i=0; i<relation.data.size(); i++) {
                ArrayList<String> tuple = relation.data.get(i);
                newTuples.addAll(createAtomicDataTuples(nonAtomicAttributeParse(tuple.get(attributeIndex)), tuple, attributeIndex));
            }
            relation.data = (ArrayList<ArrayList<String>>) newTuples.clone();
            newTuples.clear();
        }
    }

    /**
     * Creates a new tuple for atomic data in a non-atomic attribute.
     * @param atomicData the atomic data contained in the non-atomic attribute.
     * @param tuple the original tuple that contained the non-atomic attribute.
     * @param attributeIndex the index of the non-atomic data in the tuple.
     * @return the non-atomic data broken into seperate tuples.
     */
    static private ArrayList<ArrayList<String>> createAtomicDataTuples(ArrayList<String> atomicData, ArrayList<String> tuple, int attributeIndex) {
        ArrayList<ArrayList<String>> newTuples = new ArrayList<>();

        for(String value : atomicData) {
            tuple.set(attributeIndex, value.strip());
            newTuples.add((ArrayList<String>) tuple.clone());
        }

        return newTuples;
    }

    /**
     * Parses user inputted non atomic data in the form of "{data1, data2, data3,..., dataN}".
     * @param data the non-atomic data.
     * @return the data split into individual strings.
     */
    static private ArrayList<String> nonAtomicAttributeParse(String data) {
        String[] atomicData = data
                .replace("{", "")
                .replace("}", "")
                .split(",");

        return new ArrayList<>(Arrays.asList(atomicData));
    }

}

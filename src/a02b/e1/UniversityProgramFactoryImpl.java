package a02b.e1;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import a02b.e1.UniversityProgram.Sector;

public class UniversityProgramFactoryImpl implements UniversityProgramFactory {

    private enum ComparationMethod {
        EQUAL,
        GREATER_OR_EQUAL,
        LESS_OR_EQUAL
    };

    private UniversityProgram general(Set<Set<Sector>> sectors, Predicate<Pair<Set<Sector>, Integer>> pred){
        return new UniversityProgram() {

            private Map<String, Pair<Sector, Integer>> courses = new HashMap<>();

            @Override
            public void addCourse(String name, Sector sector, int credits) {
                courses.put(name, new Pair<>(sector, credits));
            }

            @Override
            public boolean isValid(Set<String> courseNames) {
                Map<Set<Sector>, Integer> sums = new LinkedHashMap<>();
                for (Set<Sector> set : sectors) {
                    sums.put(set, 0);
                }
                for (String course : courseNames) {
                    for (Set<Sector> sectorSet : sectors) {
                        if(sectorSet.contains(courses.get(course).get1())){
                            sums.put(sectorSet, sums.get(sectorSet) + courses.get(course).get2());
                        }
                    }
                }
                for (Set<Sector> sectorSet : sectors) {
                    if(!pred.test(new Pair<>(sectorSet, sums.get(sectorSet)))){
                        return false;
                    }
                }
                return true;
            }
            
        };
    }

    private UniversityProgram generalCaller(Set<Set<Sector>> allSectors, Map<Set<Sector>, Pair<Integer, ComparationMethod>> expected){
        return general(allSectors, actual -> {
            Set<Sector> sectors = actual.get1();
            Integer actualSum = actual.get2();
            Integer expectedSum = expected.get(sectors).get1();
            ComparationMethod comparationMethod = expected.get(sectors).get2();
            switch (comparationMethod) {
                case EQUAL: return actualSum == expectedSum;
                case GREATER_OR_EQUAL: return actualSum >= expectedSum;
                case LESS_OR_EQUAL: return actualSum <= expectedSum;
                default: throw new IllegalStateException();
            }
        });
    }

    @Override
    public UniversityProgram flexible() {
        Map<Set<Sector>, Pair<Integer, ComparationMethod>> expected = new HashMap<>();
        expected.put(Set.of(Sector.values()), new Pair<>(60, ComparationMethod.EQUAL));
        Set<Set<Sector>> sectors = expected.keySet();
        return generalCaller(sectors, expected);
    }

    @Override
    public UniversityProgram scientific() {
        Map<Set<Sector>, Pair<Integer, ComparationMethod>> expected = new HashMap<>();
        expected.put(Set.of(Sector.values()), new Pair<>(60, ComparationMethod.EQUAL));
        expected.put(Set.of(Sector.COMPUTER_SCIENCE), new Pair<>(12, ComparationMethod.GREATER_OR_EQUAL));
        expected.put(Set.of(Sector.PHYSICS), new Pair<>(12, ComparationMethod.GREATER_OR_EQUAL));
        expected.put(Set.of(Sector.MATHEMATICS), new Pair<>(12, ComparationMethod.GREATER_OR_EQUAL));
        Set<Set<Sector>> sectors = expected.keySet();
        return generalCaller(sectors, expected);
    }

    @Override
    public UniversityProgram shortComputerScience() {
        Map<Set<Sector>, Pair<Integer, ComparationMethod>> expected = new HashMap<>();
        expected.put(Set.of(Sector.values()), new Pair<>(48, ComparationMethod.GREATER_OR_EQUAL));
        expected.put(Set.of(Sector.COMPUTER_ENGINEERING, Sector.COMPUTER_SCIENCE), new Pair<>(30, ComparationMethod.GREATER_OR_EQUAL));
        Set<Set<Sector>> sectors = expected.keySet();
        return generalCaller(sectors, expected);
    }

    @Override
    public UniversityProgram realistic() {
        Map<Set<Sector>, Pair<Integer, ComparationMethod>> expected = new HashMap<>();
        expected.put(Set.of(Sector.values()), new Pair<>(120, ComparationMethod.EQUAL));
        expected.put(Set.of(Sector.COMPUTER_ENGINEERING, Sector.COMPUTER_SCIENCE), new Pair<>(60, ComparationMethod.GREATER_OR_EQUAL));
        expected.put(Set.of(Sector.MATHEMATICS, Sector.PHYSICS), new Pair<>(18, ComparationMethod.LESS_OR_EQUAL));
        expected.put(Set.of(Sector.THESIS), new Pair<>(24, ComparationMethod.EQUAL));
        Set<Set<Sector>> sectors = expected.keySet();
        return generalCaller(sectors, expected);
    }
}

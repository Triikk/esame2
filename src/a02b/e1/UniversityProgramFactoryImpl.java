package a02b.e1;

import static a02b.e1.UniversityProgram.Sector.COMPUTER_ENGINEERING;

import java.util.HashMap;
import java.util.HashSet;
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

    private UniversityProgram generalCaller(Set<Set<Sector>> sectors, Map<Set<Sector>, Pair<Integer, ComparationMethod>> comparation){
        return general(sectors, pair -> {
            Set<Sector> sectorSet = pair.get1();
            Integer sum = pair.get2();
            Integer expectedSum = comparation.get(sectorSet).get1();
            ComparationMethod comparationMethod = comparation.get(sectorSet).get2();
            switch (comparationMethod) {
                case EQUAL: return sum == expectedSum;
                case GREATER_OR_EQUAL: return sum >= expectedSum;
                case LESS_OR_EQUAL: return sum <= expectedSum;
                default: throw new IllegalStateException();
            }
        });
    }

    @Override
    public UniversityProgram flexible() {
        Set<Set<Sector>> sectors = new HashSet<>();
        Set<Sector> all = Set.of(Sector.values());
        sectors.add(all);
        Map<Set<Sector>, Pair<Integer, ComparationMethod>> sectorsToExpectedSums = new HashMap<>();
        sectorsToExpectedSums.put(all, new Pair<>(60, ComparationMethod.EQUAL));
        return generalCaller(sectors, sectorsToExpectedSums);
    }

    @Override
    public UniversityProgram scientific() {
        Set<Set<Sector>> sectors = new HashSet<>();
        Set<Sector> all = Set.of(Sector.values());
        Set<Sector> compSci = Set.of(Sector.COMPUTER_SCIENCE);
        Set<Sector> physics = Set.of(Sector.PHYSICS);
        Set<Sector> math = Set.of(Sector.MATHEMATICS);
        sectors.add(all);
        sectors.add(compSci);
        sectors.add(physics);
        sectors.add(math);
        Map<Set<Sector>, Pair<Integer, ComparationMethod>> sectorsToExpectedSums = new HashMap<>();
        sectorsToExpectedSums.put(all, new Pair<>(60, ComparationMethod.EQUAL));
        sectorsToExpectedSums.put(compSci, new Pair<>(12, ComparationMethod.GREATER_OR_EQUAL));
        sectorsToExpectedSums.put(physics, new Pair<>(12, ComparationMethod.GREATER_OR_EQUAL));
        sectorsToExpectedSums.put(math, new Pair<>(12, ComparationMethod.GREATER_OR_EQUAL));
        return generalCaller(sectors, sectorsToExpectedSums);
    }

    @Override
    public UniversityProgram shortComputerScience() {
        Set<Set<Sector>> sectors = new HashSet<>();
        Set<Sector> all = Set.of(Sector.values());
        Set<Sector> compSci = Set.of(Sector.COMPUTER_ENGINEERING, Sector.COMPUTER_SCIENCE);
        sectors.add(all);
        sectors.add(compSci);
        Map<Set<Sector>, Pair<Integer, ComparationMethod>> sectorsToExpectedSums = new HashMap<>();
        sectorsToExpectedSums.put(all, new Pair<>(48, ComparationMethod.GREATER_OR_EQUAL));
        sectorsToExpectedSums.put(compSci, new Pair<>(30, ComparationMethod.GREATER_OR_EQUAL));
        return generalCaller(sectors, sectorsToExpectedSums);
    }

    @Override
    public UniversityProgram realistic() {
        Set<Set<Sector>> sectors = new HashSet<>();
        Set<Sector> all = Set.of(Sector.values());
        Set<Sector> compSci = Set.of(Sector.COMPUTER_ENGINEERING, Sector.COMPUTER_SCIENCE);
        Set<Sector> mathPhys = Set.of(Sector.MATHEMATICS, Sector.PHYSICS);
        Set<Sector> thesis = Set.of(Sector.THESIS);
        sectors.add(all);
        sectors.add(compSci);
        sectors.add(mathPhys);
        sectors.add(thesis);
        Map<Set<Sector>, Pair<Integer, ComparationMethod>> sectorsToExpectedSums = new HashMap<>();
        sectorsToExpectedSums.put(all, new Pair<>(120, ComparationMethod.EQUAL));
        sectorsToExpectedSums.put(compSci, new Pair<>(60, ComparationMethod.GREATER_OR_EQUAL));
        sectorsToExpectedSums.put(mathPhys, new Pair<>(18, ComparationMethod.LESS_OR_EQUAL));
        sectorsToExpectedSums.put(thesis, new Pair<>(24, ComparationMethod.EQUAL));
        return generalCaller(sectors, sectorsToExpectedSums);
    }
}

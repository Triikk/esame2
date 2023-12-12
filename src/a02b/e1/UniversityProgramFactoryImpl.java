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

    @Override
    public UniversityProgram flexible() {
        Set<Set<Sector>> sectors = new HashSet<>();
        Set<Sector> allSectors = Set.of(Sector.values());
        sectors.add(allSectors);
        return general(sectors, (pair) -> pair.get2() == 60);
    }

    @Override
    public UniversityProgram scientific() {
        Set<Set<Sector>> sectors = new HashSet<>();
        Set<Sector> allSectors = Set.of(Sector.values());
        Set<Sector> mathSectors = Set.of(Sector.MATHEMATICS);
        Set<Sector> physicsSectors = Set.of(Sector.PHYSICS);
        Set<Sector> computerScienceSectors = Set.of(Sector.COMPUTER_SCIENCE);
        sectors.add(allSectors);
        sectors.add(mathSectors);
        sectors.add(physicsSectors);
        sectors.add(computerScienceSectors);
        return general(sectors, pair -> {
            Set<Sector> sector = pair.get1();
            Integer sum = pair.get2();
            System.out.println(sum);
            if(sector.equals(allSectors)){
                return sum == 60;
            } else if(sector.equals(mathSectors) || sector.equals(physicsSectors) || sector.equals(computerScienceSectors)) {
                return sum >= 12;
            } else {
                throw new IllegalStateException();
            }
        });
    }

    @Override
    public UniversityProgram shortComputerScience() {
        Set<Set<Sector>> sectors = new HashSet<>();
        Set<Sector> allSectors = Set.of(Sector.values());
        Set<Sector> computerScienceSectors = Set.of(Sector.COMPUTER_SCIENCE, COMPUTER_ENGINEERING);
        sectors.add(allSectors);
        sectors.add(computerScienceSectors);
        return general(sectors, pair -> {
            Set<Sector> sector = pair.get1();
            Integer sum = pair.get2();
            System.out.println(sum);
            if(sector.equals(allSectors)){
                return sum >= 48;
            } else if(sector.equals(computerScienceSectors)) {
                return sum >= 30;
            } else {
                throw new IllegalStateException();
            }
        });
    }

    @Override
    public UniversityProgram realistic() {
        Set<Set<Sector>> sectors = new HashSet<>();
        Set<Sector> allSectors = Set.of(Sector.values());
        Set<Sector> computerScienceSectors = Set.of(Sector.COMPUTER_SCIENCE, COMPUTER_ENGINEERING);
        Set<Sector> mathPhysicsSectors = Set.of(Sector.MATHEMATICS, Sector.PHYSICS);
        Set<Sector> thesisSectors = Set.of(Sector.THESIS);
        sectors.add(allSectors);
        sectors.add(computerScienceSectors);
        sectors.add(mathPhysicsSectors);
        sectors.add(thesisSectors);
        return general(sectors, pair -> {
            Set<Sector> sector = pair.get1();
            Integer sum = pair.get2();
            System.out.println(sum);
            if(sector.equals(allSectors)){
                return sum == 120;
            } else if(sector.equals(computerScienceSectors)) {
                return sum >= 60;
            } else if(sector.equals(mathPhysicsSectors)) {
                return sum <= 18;
            } else if(sector.equals(thesisSectors)) {
                return sum == 24;
            } else {
                throw new IllegalStateException();
            }
        });
    }

}

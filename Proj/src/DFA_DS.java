import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DFA_DS{
    DFA_DS(String[] alphabet){
        Alphabet = alphabet;
    }

    public List<State> AllState = new ArrayList<>(1);
    public List<State> InitialStates = new ArrayList<>(1);
    public List<State> FinalStates = new ArrayList<>(1);
    public String[] Alphabet;
    public List<Relation> relations = new ArrayList<>(1);

    public static DFA_DS makeDFA (){
        Scanner s = new Scanner(System.in);
        int stateNum;
        try {
            stateNum = s.nextInt();
        }catch (Exception e) {
            System.out.println("Enter the number of states in first line.");
            System.out.println(e.getMessage());
            return null;
        }
        DFA_DS DFA = new DFA_DS(s.next().split(","));
        String rel;
        String[] temp;
        while(true) {
            rel = s.next();
            if(!rel.equals("-1")) {
                temp = rel.split(",");
                if (temp.length >= 3)
                    DFA.relations.add(
                            new Relation(NFA_DS.AddStates(temp[0], DFA.AllState),
                                    temp[1],
                                    NFA_DS.AddStates(temp[2], DFA.AllState))
                    );
                StateKind(temp[0],DFA);
                StateKind(temp[2],DFA);
            }
            else break;
        }
        return DFA;
    }

    private static void StateKind(String stateName , DFA_DS DFA){
        if(stateName.substring(0,2).equals("->")) {
            for (State s:DFA.InitialStates) {
                if(s.StateName.equals(stateName)){
                    return;
                }
            }
            for (State s:DFA.AllState) {
                if(s.StateName.equals(stateName)) {
                    DFA.InitialStates.add(s);
                    return;
                }
            }
            return;
        }else if(stateName.substring(0,1).equals("*")){
            for (State s:DFA.FinalStates) {
                if(s.StateName.equals(stateName)){
                    return;
                }
            }
            for (State s:DFA.AllState) {
                if(s.StateName.equals(stateName)) {
                    DFA.FinalStates.add(s);
                    return;
                }
            }
        }
        return;
    }

}

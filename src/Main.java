import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class Main {

    static public DFA_DS NfaToDfa(NFA_DS nfa){
        if(nfa != null) {
            DFA_DS dfa = new DFA_DS(nfa.alphabet);
            List<MatchState> matchStates = new ArrayList<>(1);
            int stateNum = 0;
            State initial = new State("->q"+stateNum);
            stateNum++;
            matchStates.add(new MatchState(initial,nfa.InitialStates));
            dfa.AllState.add(initial);
            dfa.InitialStates.add(initial);

            int lev = 0;
            State indfaState;
            List<State> toNfaState = new ArrayList<>();
            List<State> nfaStates = new ArrayList<>();
            while(true){
                try {
                    indfaState = dfa.AllState.get(lev);
                }catch (Exception e){
                    break;
                }
                for (MatchState m:matchStates) {
                    if(m.DfaState.equals(indfaState)){
                        nfaStates = m.MatchedNfaState;
                        break;
                    }
                }
                for (String alpha:dfa.Alphabet) {
                    for (State s : nfaStates) {
                        for (Relation r : nfa.relations) {
                            if ((r.FromState.StateName.equals(s.StateName) && r.ByAlphabet.equals(alpha)))
                                //||(r.FromState.StateName.equals(s.StateName) && r.ByAlphabet.equals("_")))
                                toNfaState.add(r.ToState);
                        }
                    }
                    checklambdatransition(toNfaState , nfa);
                    deleteDuplicate(toNfaState);
                    State temp1 = ContainMatchState(matchStates , toNfaState);
                    if(temp1 == null){
                        State temp = new State("q"+stateNum);
                        matchStates.add(new MatchState(temp,toNfaState));
                        dfa.AllState.add(temp);
                        dfa.relations.add(new Relation(indfaState,alpha,temp));
                        stateNum++;
                    }else{
                        dfa.relations.add(new Relation(indfaState,alpha,temp1));
                    }
                    toNfaState = new LinkedList<>();
                }

                lev++;
            }
            SetFinalState(dfa,matchStates,nfa);
            return dfa;
        }else return null;
    }

    private static void SetFinalState(DFA_DS dfa, List<MatchState> matchStates,NFA_DS nfa) {
        List<State> nfaStates = new ArrayList<>();
        for (State dfaState:dfa.AllState) {
            for (MatchState m : matchStates) {
                if (m.DfaState.equals(dfaState)) {
                    nfaStates = m.MatchedNfaState;
                    break;
                }
            }
            for (State nfaState:nfaStates) {
                if(nfa.FinalStates.contains(nfaState)) {
                    dfaState.StateName = "*"+dfaState.StateName;
                    dfa.FinalStates.add(dfaState);
                }
            }
        }
    }

    private static void deleteDuplicate(List<State> toNfaState) {
        for(int i=0;i<=toNfaState.size();i++) {
            for(int j=i+1;j<=toNfaState.size()-1;j++) {
                if(toNfaState.get(i).equals(toNfaState.get(j)))
                    toNfaState.remove(j);
            }
        }
    }

    private static void checklambdatransition(List<State> toNfaState ,NFA_DS nfa) {
        List<State> addition = new ArrayList<>();
        for (State s:toNfaState) {
            for (Relation r:nfa.relations) {
                if(s.equals(r.FromState) && r.ByAlphabet.equals("_"))
                    addition.add(r.ToState);
            }
        }
        for (State s:addition) {
            toNfaState.add(s);
        }
        return;
    }

    static private State ContainMatchState(List<MatchState> matchStates, List<State> toNfaState) {
        for (MatchState m: matchStates) {
            if(m.MatchedNfaState.containsAll(toNfaState) &&
            toNfaState.containsAll(m.MatchedNfaState)){
                return m.DfaState;
            }
        }
        return null;
    }

    private static void Print_DFA(DFA_DS dfa) {
        System.out.println(dfa.AllState.size());
        int counter = 0;
        for(;counter<dfa.Alphabet.length-1;counter++)
            System.out.print(dfa.Alphabet[counter]+",");
        System.out.print(dfa.Alphabet[counter]+"\n");

        for(Relation r:dfa.relations)
            System.out.println(r.FromState.StateName+","+r.ByAlphabet+","+r.ToState.StateName);
        return;

    }

    public static DFA_DS MinimizeDFA(DFA_DS dfa) {
        List<MinimizeTable> minimizeTables = new ArrayList<>();
        int stateNum = 0;
        List<State> tempStates = new ArrayList<>();
        for (State s: dfa.AllState) {
            if(!dfa.FinalStates.contains(s))
                tempStates.add(s);
        }
        minimizeTables.add(new MinimizeTable(tempStates,new State("q"+stateNum),dfa));
        stateNum++;
        minimizeTables.add(new MinimizeTable(dfa.FinalStates,new State("q"+stateNum),dfa));
        FillToMinimizedState(minimizeTables);
        List<MinimizeTable> minimizeTables1 = new ArrayList<>();
        while (true){
            stateNum = 0;
            boolean flag = true;
            for(MinimizeTable m:minimizeTables){
                List<State> usedState = new ArrayList<>();
                for (int i=0;i<m.InternalStates.size();i++) {
                    tempStates = new ArrayList<>();
                    if(usedState.contains(m.InternalStates.get(i)))
                        continue;
                    tempStates.add(m.InternalStates.get(i));
                    usedState.add(m.InternalStates.get(i));
                    for (int j=i+1;j<m.InternalStates.size();j++) {
                        if(isEquals(m.ToMinimizedStateByAlpha.get(i),m.ToMinimizedStateByAlpha.get(j))) {//مساوی کار نمیکنه
                            tempStates.add(m.InternalStates.get(j));
                            usedState.add(m.InternalStates.get(j));
                        }
                    }
                    if(tempStates.containsAll(m.InternalStates) && m.InternalStates.containsAll(tempStates)) {
                        minimizeTables1.add(new MinimizeTable(m.InternalStates, new State("q" + stateNum), dfa));
                        stateNum++;
                        break;
                    }
                    for (MinimizeTable m1:minimizeTables1) {
                        if(m1.InternalStates.containsAll(tempStates)&& tempStates.containsAll(m1.InternalStates)) {
                            flag = false;
                        }
                    }
                    if(flag) {
                        minimizeTables1.add(new MinimizeTable(tempStates, new State("q" + stateNum), dfa));
                        stateNum++;
                    }else flag = true;
                }
            }
            FillToMinimizedState(minimizeTables1);
            if(CheckIsMinimized(minimizeTables1)){
                return MakeDFA(minimizeTables1,dfa);
            }

        }
    }

    private static DFA_DS MakeDFA(List<MinimizeTable> minimizeTables, DFA_DS dfa) {
        DFA_DS resultDFA = new DFA_DS(dfa.Alphabet);
        for (MinimizeTable m:minimizeTables) {
            for (State s:m.InternalStates) {
                if(dfa.InitialStates.contains(s)){
                    m.Minimized.StateName = "->" + m.Minimized.StateName;
                    resultDFA.InitialStates.add(m.Minimized);
                    break;
                }else if(dfa.FinalStates.contains(s)){
                    m.Minimized.StateName = "*" + m.Minimized.StateName;
                    resultDFA.FinalStates.add(m.Minimized);
                    break;
                }
            }
            resultDFA.AllState.add(m.Minimized);
        }
        for (MinimizeTable m: minimizeTables) {
            for (int i=0;i<dfa.Alphabet.length;i++) {
                resultDFA.relations.add(new Relation(m.Minimized,dfa.Alphabet[i],m.ToMinimizedStateByAlpha.get(0)[i]));
            }
        }
        return resultDFA;
    }

    private static boolean CheckIsMinimized(List<MinimizeTable> minimizeTables1) {
        for (MinimizeTable m:minimizeTables1) {
            for(int i=1;i<m.ToMinimizedStateByAlpha.size();i++){
                if(!isEquals(m.ToMinimizedStateByAlpha.get(0),m.ToMinimizedStateByAlpha.get(i)))
                   return false;
            }
        }
        return true;
    }

    private static boolean isEquals(State[] states, State[] states1) {
        for (int i =0;i<states.length;i++) {
            if(!states[i].equals(states1[i])){
                return false;
            }
        }
        return true;
    }

    public static void FillToMinimizedState (List<MinimizeTable> minimizeTables){
        for (MinimizeTable m:minimizeTables) {
            for (State[] states:m.ToDfaStateByAlpha) {
                State[] minimizedStates = new State[states.length];
                for(int i=0 ; i<states.length;i++){
                    for (MinimizeTable m1:minimizeTables) {
                        if(m1.InternalStates.contains(states[i]))
                            minimizedStates[i] = m1.Minimized;
                    }

                }
                m.ToMinimizedStateByAlpha.add(minimizedStates);
            }
        }
    }

    public static void main(String[] args) {

        String input;
        Scanner scanner = new Scanner(System.in);
        String filePath = "file.txt";
        NFA_DS nfa = null;
        DFA_DS dfa = null;
        File file = new File(filePath);
        try {
            file.createNewFile();
            while (true) {
                FileWriter fileWriter = new FileWriter(file);
                System.out.println("Chose a number : ");
                System.out.println("Enter (1)to give a file path to save(DFA or NFA): ");
                System.out.println("(2)to give a NFA");
                System.out.println("(3)to give a DFA or write on file.");
                System.out.println("(4)to convert NFA to DFA.");
                System.out.println("(5)to minimization DFA.");
                input = scanner.next();
                switch (input) {
                    case "1": {
                        filePath = scanner.next();
                        file.createNewFile();
                    }
                    break;
                    case "2": {
                        nfa = new NFA_DS();
                    }
                    break;
                    case "3": {
                        System.out.println("chose (1) to load DFA or (2) to enter a DFA : ");
                        input = scanner.next();
                        if (input.equals("1")) {
                            Write_DFA(dfa,fileWriter);
                        } else if (input.equals("2")) {
                            dfa = DFA_DS.makeDFA();
                        }
                    } break;
                    case "4": {
                        dfa = NfaToDfa(nfa);
                        Print_DFA(dfa);
                    }
                    break;
                    case "5": {
                        dfa = MinimizeDFA(dfa);
                        Print_DFA(dfa);
                    }

                    default: {
                        System.out.println("!!!ERROR!!!!(Enter correct number.)");
                    }

                }
            }
        }catch (Exception e){

        }

    }

    private static void Write_DFA(DFA_DS dfa , FileWriter fileWriter) {
        int counter = 0;
        String temp = null;
        try {
            fileWriter.write(dfa.AllState.size());
            for (; counter < dfa.Alphabet.length - 1; counter++)
                temp += dfa.Alphabet[counter] + ",";
            temp += dfa.Alphabet[counter];
            fileWriter.write(temp);
            for (Relation r : dfa.relations)
                fileWriter.write(r.FromState.StateName + "," + r.ByAlphabet + "," + r.ToState.StateName);
            return;
        }catch (Exception e){
            return;
        }
    }

}

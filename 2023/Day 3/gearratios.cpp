#include <bits/stdc++.h>

using namespace std;

bool inBounds(int r, int c, vector<vector<char>> &map){
    return (0 <= r && r < map.size()) && (0 <= c && c < map.at(0).size());
}

int clearNumAtPosition(int r, int c, vector<vector<char>> &map){
    if(!isdigit(map[r][c])){
        return 0;
    }
    int startPos = -1;
    int endPos = -1;
    int cursor = c;
    // cursor will start at a digit
    while(inBounds(r, cursor - 1, map) && isdigit(map[r][cursor - 1])){ // go left
        cursor--;
    }
    startPos = cursor;
    cursor = c;
    while(inBounds(r, cursor + 1, map) && isdigit(map[r][cursor + 1])){ // go right
        cursor++;
    }
    endPos = cursor;
    string number;
    for(int i = startPos; i <= endPos; i++){
        number += map[r][i];
        map[r][i] = '.';
    }
    return stoi(number);
}

int getNumAtPosition(int r, int c, vector<vector<char>> &map){
    if(!isdigit(map[r][c])){
        return -1;
    }
    int startPos = -1;
    int endPos = -1;
    int cursor = c;
    // cursor will start at a digit
    while(inBounds(r, cursor - 1, map) && isdigit(map[r][cursor - 1])){ // go left
        cursor--;
    }
    startPos = cursor;
    cursor = c;
    while(inBounds(r, cursor + 1, map) && isdigit(map[r][cursor + 1])){ // go right
        cursor++;
    }
    endPos = cursor;
    string number;
    for(int i = startPos; i <= endPos; i++){
        number += map[r][i];
    }
    return stoi(number);
}

int totalParts(vector<vector<char>> &map){
    int sum = 0;
    for(int r = 0; r < map.size(); r++){
        for(int c = 0; c < map.at(0).size(); c++){
            if(!(isdigit(map[r][c]) || (map[r][c] == '.'))){ // if symbol
                for(int i = r - 1; i <= r + 1; i++){
                    for(int j = c - 1; j <= c + 1; j++){
                        if(inBounds(i, j, map)){
                            int number = clearNumAtPosition(i, j, map);
                            sum += number;
                        }
                    }
                }
            }
        }
    }
    return sum;
}

int countNumbersAround(int r, int c, vector<vector<char>> &map, int& num1, int& num2){
    int count = 0;
    num1 = -1;
    num2 = -1;
    vector<vector<char>> mapcopy;
    for(int r = 0; r < map.size(); r++){
        mapcopy.push_back(vector<char>());
        for(int c = 0; c < map.at(0).size(); c++){
            mapcopy.at(r).push_back(map[r][c]);
        }
    }
    for(int i = r - 1; i <= r + 1; i++){
        for(int j = c - 1; j <= c + 1; j++){
            if(inBounds(i, j, map)){
                int number = getNumAtPosition(i, j, mapcopy);
                if(number != -1){
                    clearNumAtPosition(i, j, mapcopy);
                    count++;
                    if(num1 == -1){
                        num1 = number;
                    }
                    else if(num2 == -1){
                        num2 = number;
                    }
                }
            }
        }
    }
    return count;
}

int totalGearRatios(vector<vector<char>> &map){
    int sum = 0;
    for(int r = 0; r < map.size(); r++){
        for(int c = 0; c < map.at(0).size(); c++){
            if(map[r][c] == '*'){ // if *
                int num1, num2;
                
                if(countNumbersAround(r, c, map, num1, num2) == 2){
                    sum += (num1 * num2);
                }
            }
        }
    }
    return sum;
}

int main(){
    ifstream inputFile("gearratios2.txt");
    string line;
    vector<vector<char>> map;
    while(getline(inputFile, line)){
        map.push_back(vector<char>());
        for(int i = 0; i < line.length(); i++){
            if(!isspace(line.at(i)))
                map.at(map.size()-1).push_back(line.at(i));
        }
    }
    //cout << totalParts(map) << endl;
    cout << totalGearRatios(map) << endl;
    /*for(int r = 0; r < map.size(); r++){
        for(int c = 0; c < map.at(0).size(); c++){
            cout << map[r][c];
        }
        cout << endl;
    }*/
    return 0;
}

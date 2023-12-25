#include <bits/stdc++.h>

using namespace std;

int main(){
    ifstream inputFile("trebuchetpart2_2.txt");
    int sum = 0;
    string line;
    while(getline(inputFile, line)){
        //int digit1;
        //int digit2;
        /*int i = 0;
        while(i < line.length()){
            if(isdigit(line.at(i))){
                digit1 = line.at(i) - '0';
                break;
            }
            i++;
        }
        i = line.length() - 1;
        while(i >= 0){
            if(isdigit(line.at(i))){
                digit2 = line.at(i) - '0';
                break;
            }
            i--;
        }
        sum += digit1 * 10 + digit2;
        cout << digit1 << digit2 << endl;*/
        line = line.substr(0, line.length()-1);
        pair<size_t, int> firstDigitPositionValue = pair<size_t, int>(string::npos, -1);
        pair<size_t, int> lastDigitPositionValue = pair<size_t, int>(string::npos, -1);
        string tokens[20] = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "zero", "one", "two", "three", "four",
            "five", "six", "seven", "eight", "nine"
        };
        pair<size_t, int> leftPositions[20] = {pair<size_t, int>(string::npos, -1)};
        pair<size_t, int> rightPositions[20] = {pair<size_t, int>(string::npos, -1)};
        for(int i = 0; i < 20; i++){
            leftPositions[i] = pair<size_t, int>(line.find(tokens[i]), i % 10);
            rightPositions[i] = pair<size_t, int>(line.rfind(tokens[i]), i % 10);
        }
        firstDigitPositionValue = leftPositions[0];
        lastDigitPositionValue = rightPositions[0];
        int n = 0;
        while(firstDigitPositionValue.first == string::npos || lastDigitPositionValue.first == string::npos){
            if(leftPositions[n].first != string::npos)
                firstDigitPositionValue = leftPositions[n];
            if(rightPositions[n].first != string::npos)
                lastDigitPositionValue = rightPositions[n];
            n++;
        }
        for(int i = 0; i < 20; i++){
            if(leftPositions[i].first != string::npos && leftPositions[i].first < firstDigitPositionValue.first){
                firstDigitPositionValue = leftPositions[i];
            }
            if(rightPositions[i].first != string::npos && rightPositions[i].first > lastDigitPositionValue.first){
                lastDigitPositionValue = rightPositions[i];
            }
        }
        cout << "FOUND DIGITS: " << firstDigitPositionValue.second << lastDigitPositionValue.second << endl;
        sum += (firstDigitPositionValue.second * 10) + lastDigitPositionValue.second;
    }
    cout << sum << endl;

    return 0;
}

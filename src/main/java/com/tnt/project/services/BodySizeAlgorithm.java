package com.tnt.project.services;

import org.springframework.stereotype.Component;

@Component
public class BodySizeAlgorithm {

       public String evaluate(double shoulder, double bust, double waist, double hip) {

           // 모래시계형 (X)
           if (Math.abs(shoulder - hip) <= 3 && (waist / hip <= 0.75)) {
               return "X";
           }

           // 삼각형 (A)
           if (hip - shoulder >= 4) {
               return "A";
           }

           // 역삼각형 (V)
           if (shoulder - hip >= 4) {
               return "V";
           }

           // 직사각형 (H)
           if (waist / hip >= 0.80) {
               return "H";
           }

           // 원형 (O)
           if (waist > bust || waist > hip) {
               return "O";
           }

           return "H";
       }
   }

package org.stormdev.mcwipeout.frame.io.sheets;
/*
  Created by Stormbits at 10/27/2023
*/

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class SheetTeam {

    private String shortHand;
    private String color;
    private List<String> playerNames;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devj130_lab4;

/**
 *
 * @author bezdetk0@mail.ru
 */
public enum LaunchEnum {
    SERVER("server"),
    CLIENT("client");

    private final String typeOfLaunch;

    private LaunchEnum(String typeOfLaunch) {
        this.typeOfLaunch = typeOfLaunch;
    }
    
}

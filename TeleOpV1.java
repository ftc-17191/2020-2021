package org.firstinspires.ftc.teamcode;

import java.lang.Thread;
import java.lang.String;
import java.lang.Math;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "TeleOp")
public class TeleOpV1 extends LinearOpMode {

    private DcMotor motor1;
    private DcMotor motor2;
    private DcMotor motor3;
    private DcMotor motor4;
    private DcMotor motor5;
    private DcMotor motor6;
    private DcMotor motor7;
    private Servo servo1;
    private Servo servo2;


    @Override
    public void runOpMode() {
        // Debugging variables
        double gripperStartingPosition = .5;

        // DO NOT TOUCH THESE VARIABLES UNLESS NEEDED
        double vertical;
        float horizontal;
        float pivot;
        double grip = gripperStartingPosition;
        double launcher = 0;
        double tumbler = 0;
        double lservo = 0;
        double armpower = 0;
        boolean goBack = false;
        String errormessage = "";


        // Initialising motors, directions, and run modes
        DcMotor   motor1 = hardwareMap.get(DcMotor.class,   "motor1");
        DcMotor   motor2 = hardwareMap.get(DcMotor.class,   "motor2");
        DcMotor   motor3 = hardwareMap.get(DcMotor.class,   "motor3");
        DcMotor   motor4 = hardwareMap.get(DcMotor.class,   "motor4");
        DcMotor   motor5 = hardwareMap.get(DcMotor.class,   "motor5");
        DcMotor   motor6 = hardwareMap.get(DcMotorEx.class, "motor6");
        DcMotor   motor7 = hardwareMap.get(DcMotor.class,   "motor7");
        Servo     servo1 = hardwareMap.get(Servo.class,     "servo1");
        Servo     servo2 = hardwareMap.get(Servo.class,     "servo2");

        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor5.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor6.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor7.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);




        waitForStart();
        if (opModeIsActive()) {

            vertical = gamepad1.right_stick_y;
            horizontal = -gamepad1.left_stick_x;
            pivot = gamepad1.right_stick_x;
            grip = gripperStartingPosition;
            launcher = 0;
            telemetry.clearAll();

            while (opModeIsActive()) {

                // Gripper Control
                if (gamepad2.dpad_left ) {
                    if (grip > 0) {
                        grip = grip - .1;
                    }
                } else if (gamepad2.dpad_right) {
                    if (grip < 1) {
                        grip = grip + .1;
                    }
                }


                // Arm Control
                if (gamepad2.dpad_up) {
                    armpower = 1;
                } else if (gamepad2.dpad_down) {
                    armpower = -1;
                } else {
                    motor7.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    armpower = 0;
                }

                // Launching Servo

                if (gamepad2.right_trigger > .5) {
                    lservo = 1;
                } else {
                    lservo = 0;
                }

                // Launching Motor Control
                if (gamepad2.b) {
                    if (launcher == -.97) {
                        launcher = 0;
                    } else {
                        launcher = -.97;
                    }
                    while (gamepad2.b) {
                        try {
                            Thread.sleep(50);
                        } catch (Exception Ex) {
                            errormessage = "THREAD ERROR CAUGHT";
                        }
                    }
                }

                // Tumbler Motor Control
                if (gamepad2.a) {
                    if (tumbler == 0 || tumbler == -1) {
                        tumbler = 1;
                    } else {
                        tumbler = 0;
                    }
                    while (gamepad2.a) {
                        try {
                            Thread.sleep(50);
                        } catch (Exception Ex) {
                            errormessage = "THREAD ERROR CAUGHT";
                        }
                    }
                }
                if (gamepad2.back) {
                    tumbler = -1;
                }

                // Motors and Servos
                vertical = -gamepad1.right_stick_y * Math.abs(-gamepad1.right_stick_y);
                horizontal = gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x);
                pivot = gamepad1.right_stick_x * Math.abs(gamepad1.right_stick_x);

                motor1.setPower(pivot + (vertical - horizontal));
                motor2.setPower(-pivot + (vertical - horizontal));
                motor3.setPower(pivot + -vertical + -horizontal);
                motor4.setPower((-pivot + -vertical + -horizontal));
                motor5.setPower(tumbler);
                motor6.setPower(launcher);
                motor7.setPower(armpower);


                servo1.setPosition(grip);
                servo2.setPosition(lservo);
                telemetry.clearAll();
                telemetry.addLine("H: " + horizontal + " V: " + vertical + " P: " + pivot);
                telemetry.addLine("Arm Power: " + armpower + " Grip: " + grip);
                telemetry.addLine("L Servo: " + lservo + " Launcher: " + launcher);
                telemetry.addLine("Tumbler: " + tumbler + " Back: " + gamepad2.back);
                telemetry.addLine("Errors: " + motor7.getTargetPosition());

                telemetry.update();

            }
        }
    }
}
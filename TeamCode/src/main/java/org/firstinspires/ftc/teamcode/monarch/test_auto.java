package org.firstinspires.ftc.teamcode.monarch;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name="busted auto",group="Monarch")
public class monarch_auto extends LinearOpMode {


    private DcMotor backLeft = null;

    private DcMotor backRight = null;

    @Override
    public void runOpMode(){

        telemetry.addData("Status","running, at the very least.");

        //setup
        backLeft = hardwareMap.get(DcMotor.class, "motorFrontLeft");
        backRight = hardwareMap.get(DcMotor.class, "motorBackRight");

        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        waitForStart();

        //toggle powers
        backRight.setPower(.5);
        backLeft.setPower(.5);

        sleep(1500);

        telemetry.addData("Status","finished");
        telemetry.update();

        backRight.setPower(0.0);
        backLeft.setPower(0.0);
    }
}

package org.firstinspires.ftc.teamcode.monarch;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Test Launch", group="Monarch")
public class Test extends OpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor frontLeft = null;
    private DcMotor backRight = null;
    private DcMotor intake = null;
    private DcMotor midtake = null;
    private DcMotor launch = null;

    double speed_modifier = 1.0; // scale the speed
    ElapsedTime SpeedInputCD = new ElapsedTime();
    ElapsedTime LaunchInputCD = new ElapsedTime();
    ElapsedTime LaunchTimer = new ElapsedTime();

    private double launchPower = .5;
    private double intakePower = 0.5;

    private double drivePower = 0.5;
    private long nextUpdate;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        nextUpdate = System.currentTimeMillis();


        launch = hardwareMap.get(DcMotor.class, "launch");


        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop() {
        telemetry.addData("Version", "1.1.0");

        testLaunch();
        telemetry.update();
    }

    private void testIntake() {

        if (gamepad1.left_bumper)
        {
            if (System.currentTimeMillis() > nextUpdate)
            {
                intakePower = intakePower - 0.1;
                nextUpdate = System.currentTimeMillis() + 1000; // One second delay before you can update again
            }
        }
        else if (gamepad1.right_bumper)
        {
            if (System.currentTimeMillis() > nextUpdate)
            {
                intakePower = intakePower + 0.1;
                nextUpdate = System.currentTimeMillis() + 1000; // One second delay before you can update again
            }
        }

        if (gamepad1.a)
        {
            intake.setPower(intakePower);
        }
        else
        {
            intake.setPower(0);
        }

        telemetry.addData("Intake Power", intakePower);

    }

    private void testLaunch() { // please replace with pid system later
        if (gamepad1.y && LaunchInputCD.seconds() > .35) {
            launchPower += .1;

            LaunchInputCD.reset();
        } else if (gamepad1.b && LaunchInputCD.seconds() > .35) {
            launchPower -= .1;

            LaunchInputCD.reset();
        }

        telemetry.addData("launchPower", launchPower);

        //we keep the launch power in check
        launchPower = Math.max(0.0, (Math.min(1.0, launchPower)));

        if (gamepad1.y) {
            LaunchTimer.reset();

            launch.setPower(launchPower);

            //launch.setPower(launchPower);

            // } else if (launch.getPower()>.05){
            //   if (LaunchTimer.seconds()>.3){
            //     launch.setPower(0.0);
            //}
            //}

        }
        else
        {
            launch.setPower(0);
        }
    }

   private void driverPower() {

        if (gamepad1.left_trigger_pressed)
        {
            if (System.currentTimeMillis() > nextUpdate)
            {
                drivePower = drivePower -0.5;
                nextUpdate = System.currentTimeMillis() + 1000;
            }
        }
        else if (gamepad1.right_trigger_pressed)
        {
            if (System.currentTimeMillis() > nextUpdate)
            {
                drivePower = drivePower + 0.5;
                nextUpdate = System.currentTimeMillis() + 1000;
            }
        }

    }

    private void testDrive() {
        double forward = gamepad1.left_stick_y * -1;
        double right = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;

        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        double maxSpeed = 1.0;  // make this slower for outreaches

        // We multiply by maxSpeed so that it can be set lower for outreaches
        // When a young child is driving the robot, we may not want to allow full
        // speed.
        frontLeft.setPower(maxSpeed * frontLeftPower);
        frontRight.setPower(maxSpeed * frontRightPower);
        backLeft.setPower(maxSpeed * backLeftPower);
        backRight.setPower(maxSpeed * backRightPower);
    }


    // drive Mecanum robot centric
    private void drive() {


        // input for speed and driving control
        double axial   = -gamepad1.left_stick_y;
        double lateral =  gamepad1.left_stick_x;
        double yaw     =  gamepad1.right_stick_x;

        double frontLeftPower  = axial + lateral + yaw;
        double frontRightPower = axial - lateral - yaw;
        double backLeftPower   = axial - lateral + yaw;
        double backRightPower  = axial + lateral - yaw;

        if (gamepad1.left_bumper&& SpeedInputCD.seconds()>.35){
            speed_modifier-=.1;
            speed_modifier = Math.max(.1,speed_modifier);

            SpeedInputCD.reset();
        } else if (gamepad1.right_bumper&& SpeedInputCD.seconds()>.35){
            speed_modifier+=.1;
            speed_modifier = Math.min(1.0,speed_modifier);

            SpeedInputCD.reset();
        }

        //Modify and keep power under 1.0
        double max;
        max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        //much better and simpler way to cap
        double denominator = Math.max(1.0,max);

        double calcFrontLeftPower = (frontLeftPower/denominator)*speed_modifier;
        double calcFrontRightPower = (frontRightPower/denominator)*speed_modifier;
        double calcBackLeftPower = (backLeftPower/denominator)*speed_modifier;
        double calcBackRightPower = (backRightPower/denominator)*speed_modifier;

        // Send calculated power to wheels
        frontLeft.setPower(calcFrontLeftPower);
        frontRight.setPower(calcFrontRightPower);
        backLeft.setPower(calcBackLeftPower);
        backRight.setPower(calcBackRightPower);

        intake.setPower(gamepad1.left_trigger);
        midtake.setPower(gamepad1.right_trigger);

        telemetry.addData("Speed-Cap", speed_modifier);
        telemetry.addData("Front left/Right", "%4.2f, %4.2f", calcFrontLeftPower, calcFrontRightPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", calcBackLeftPower, calcBackRightPower);
        telemetry.addData("Intake Power", intake.getPower());
        telemetry.addData("Mid-take Power", midtake.getPower());
    }
    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}



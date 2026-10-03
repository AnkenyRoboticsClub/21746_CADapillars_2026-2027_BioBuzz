package org.firstinspires.ftc.teamcode.monarch;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Monarch v1.0.3", group="Monarch")
public class Monarch extends OpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor frontLeft = null;
    private DcMotor backRight = null;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing");

        // Initialize all 4 motors
        backLeft = hardwareMap.get(DcMotor.class, "motorFrontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "motorFrontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "motorBackLeft");
        backRight = hardwareMap.get(DcMotor.class, "motorBackRight");
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

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
        telemetry.addData("Version", "1.0.3");

        driveForwardAndBackwardOnly();

        if (gamepad1.y)
            frontRight.setPower(0.5); // Front Right

        if (gamepad1.b)
            backLeft.setPower(0.5); // Back Left

        if (gamepad1.a)
            backRight.setPower(0.5); // Back right

        if (gamepad1.x)
            frontLeft.setPower(0.5); // Front Left

        telemetry.update();
    }

    private void driveForwardAndBackwardOnly() {
        double rightStickYRaw = gamepad1.right_stick_y;
        telemetry.addData("Forward Speed (Raw)", rightStickYRaw);

        double rightStickYProcessed = -1 * rightStickYRaw;
        telemetry.addData("Forward Speed (Processed)", rightStickYProcessed);

        frontLeft.setPower(rightStickYProcessed);
        frontRight.setPower(rightStickYProcessed);
        backLeft.setPower(rightStickYProcessed);
        backRight.setPower(rightStickYProcessed);

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}

'use client';
import { Snackbar, Alert, SnackbarCloseReason } from "@mui/material";

export interface ToastProps {
    open: boolean;
    message: string;
    severity: "success" | "info" | "warning" | "error";
    setProps: (props: any) => void;
}

export default function Toast({ open, message, severity, setProps }: ToastProps) {
    const handleClose = (
        event?: React.SyntheticEvent | Event,
        reason?: SnackbarCloseReason,
    ) => {
        if (reason === 'clickaway') {
            return;
        }

        setProps({
            open: false,
            message: "",
            severity: "info" as "success" | "info" | "warning" | "error",
        });
    };

    return (
        <Snackbar open={open} autoHideDuration={6000} onClose={handleClose} anchorOrigin={{ vertical: 'top', horizontal: 'right' }}>
            <Alert severity={severity} sx={{ width: '100%' }} onClose={handleClose}>
                {message}
            </Alert>
        </Snackbar>
    );
}
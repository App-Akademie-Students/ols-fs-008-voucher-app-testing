import "./style.css";
import { createVoucher } from "./api/voucherApi";
import { onCreate, readForm } from "./ui/adminView";
import { clearError, hideVoucher, showError, showVoucher } from "./ui/dom";
import { toErrorMessage } from "./ui/errorMessage";

async function create(): Promise<void> {
  clearError();
  hideVoucher();
  const { code, amount, expirationDate } = readForm();
  if (code === "" || amount === "" || expirationDate === "") {
    showError("Bitte Code, Betrag und Ablaufdatum angeben.");
    return;
  }
  try {
    showVoucher(await createVoucher({ code, amount: Number(amount), expirationDate }));
  } catch (error) {
    showError(toErrorMessage(error));
  }
}

onCreate(() => void create());

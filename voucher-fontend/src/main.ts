import "./style.css";
import { getVoucher, redeemVoucher } from "./api/voucherApi";
import { onLookup, onRedeem, readCode } from "./ui/customerView";
import { clearError, hideVoucher, showError, showVoucher } from "./ui/dom";
import { toErrorMessage } from "./ui/errorMessage";

let currentCode: string | null = null;

async function lookup(): Promise<void> {
  clearError();
  hideVoucher();
  currentCode = null;
  const code = readCode();
  if (code === "") {
    showError("Bitte einen Code eingeben.");
    return;
  }
  try {
    const voucher = await getVoucher(code);
    currentCode = voucher.code;
    showVoucher(voucher);
  } catch (error) {
    showError(toErrorMessage(error));
  }
}

async function redeem(): Promise<void> {
  if (currentCode === null) {
    return;
  }
  clearError();
  try {
    showVoucher(await redeemVoucher(currentCode));
  } catch (error) {
    showError(toErrorMessage(error));
  }
}

onLookup(() => void lookup());
onRedeem(() => void redeem());
